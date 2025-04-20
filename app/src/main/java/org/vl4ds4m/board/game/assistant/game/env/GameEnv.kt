package org.vl4ds4m.board.game.assistant.game.env

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.States
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.changesCurrentPlayer
import org.vl4ds4m.board.game.assistant.game.changesPlayerState
import org.vl4ds4m.board.game.assistant.game.currentPlayerChangedAction
import org.vl4ds4m.board.game.assistant.game.currentPlayerIds
import org.vl4ds4m.board.game.assistant.game.log.GameActionsHistory
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.playerId
import org.vl4ds4m.board.game.assistant.game.playerStateChangedAction
import org.vl4ds4m.board.game.assistant.game.playerStates
import org.vl4ds4m.board.game.assistant.updateAndGetStates
import org.vl4ds4m.board.game.assistant.updateMap
import java.util.concurrent.atomic.AtomicLong

open class GameEnv(final override val type: GameType) : Game {
    final override val name: MutableStateFlow<String> = MutableStateFlow("")

    private val mPlayers: MutableStateFlow<Players> = MutableStateFlow(mapOf())
    final override val players: StateFlow<Players> = mPlayers.asStateFlow()

    protected val mCurrentPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    final override val currentPlayerId: StateFlow<Long?> = mCurrentPlayerId.asStateFlow()

    private val history = GameActionsHistory()

    final override fun changeCurrentPlayerId(id: Long) {
        players.value[id]
            ?.takeIf { it.active }
            ?: return
        val ids = mCurrentPlayerId.updateAndGetStates { id }
        addCurrentPlayerChangedAction(ids)
    }

    protected fun addCurrentPlayerChangedAction(ids: States<Long?>) {
        if (ids.prev == ids.next) return
        history += currentPlayerChangedAction(ids)
    }

    override fun addPlayer(netDevId: String?, name: String): Long =
        addPlayer(netDevId, name, PlayerState(0, mapOf()))

    private val nextNewPlayerId: AtomicLong = AtomicLong(0)

    protected open fun addPlayer(netDevId: String?, name: String, state: PlayerState): Long {
        val id = nextNewPlayerId.incrementAndGet()
        val player = Player(
            netDevId = netDevId,
            name = name,
            active = true,
            state = state
        )
        mPlayers.updateMap { put(id, player) }
        val ids = mCurrentPlayerId.updateAndGetStates { it ?: id }
        if (initialized.value) {
            addCurrentPlayerChangedAction(ids)
        }
        return id
    }

    override fun removePlayer(id: Long) {
        val (players, _) = remove(id) ?: return
        val ids = updateCurrentIdOnEqual(id, players) ?: return
        addCurrentPlayerChangedAction(ids)
    }

    protected fun remove(id: Long): States<Players>? = mPlayers.updateMap {
        remove(id) ?: return null
    }

    private fun updateCurrentIdOnEqual(
        playerId: Long,
        players: Players
    ): States<Long?>? = mCurrentPlayerId.updateAndGetStates { currentId ->
        if (currentId != playerId) return null
        players.firstNotNullOfOrNull { (id, player) ->
            id.takeIf { player.active && id != playerId }
        }
    }

    final override fun bindPlayer(id: Long, netDevId: String) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            put(id, player.copy(netDevId = netDevId))
        }
    }

    final override fun unbindPlayer(id: Long) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            put(id, player.copy(netDevId = null))
        }
    }

    final override fun renamePlayer(id: Long, name: String) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            if (player.name == name) return
            put(id, player.copy(name = name))
        }
    }

    override fun freezePlayer(id: Long) {
        val (players, _) = freeze(id) ?: return
        val ids = updateCurrentIdOnEqual(id, players) ?: return
        addCurrentPlayerChangedAction(ids)
    }

    protected fun freeze(playerId: Long): States<Players>? {
        return mPlayers.updateMap {
            val player = get(playerId) ?: return null
            if (!player.active) return null
            put(playerId, player.copy(active = false))
        }
    }

    final override fun unfreezePlayer(id: Long) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            if (player.active) return
            put(id, player.copy(active = true))
        }
        val ids = mCurrentPlayerId.updateAndGetStates { it ?: id }
        addCurrentPlayerChangedAction(ids)
    }

    final override fun changePlayerState(id: Long, state: PlayerState) {
        val (oldPlayers, newPlayers) = mPlayers.updateMap {
            val player = get(id) ?: return
            if (player.state == state) return
            put(id, player.copy(state = state))
        }
        val states = States(
            prev = oldPlayers[id]!!.state,
            next = newPlayers[id]!!.state
        )
        history += playerStateChangedAction(id, states)
    }

    private var startTime: Long? = null
    private var stopTime: Long? = null

    final override val timeout = MutableStateFlow(false)

    private val mSecondsToEnd = MutableStateFlow(0)
    final override val secondsToEnd: StateFlow<Int> = mSecondsToEnd.asStateFlow()

    private val mInitialized = MutableStateFlow(false)
    final override val initialized: StateFlow<Boolean> = mInitialized.asStateFlow()

    private val mCompleted = MutableStateFlow(false)
    final override val completed: StateFlow<Boolean> = mCompleted.asStateFlow()

    final override fun changeSecondsToEnd(seconds: Int) {
        if (seconds > 0) mSecondsToEnd.value = seconds
    }

    private val timer: Timer = Timer()

    final override fun initialize() {
        Log.i(TAG, "Initialize game")
        mInitialized.value = true
    }

    final override fun start() {
        Log.i(TAG, "Start game")
        if (startTime == null) {
            startTime = System.currentTimeMillis()
        }
        if (timeout.value) {
            timer.start(mSecondsToEnd, mCompleted)
        }
    }

    final override fun stop() {
        Log.i(TAG, "Stop game")
        stopTime = System.currentTimeMillis()
        timer.stop()
    }

    final override fun complete() {
        Log.i(TAG, "Complete game")
        mCompleted.value = true
    }

    final override fun returnGame() {
        Log.i(TAG, "Return to completed game")
        timeout.value = false
        mCompleted.value = false
    }

    final override val reverted: StateFlow<Boolean> = history.reverted

    final override val repeatable: StateFlow<Boolean> = history.repeatable

    final override val actions = history.actions

    final override fun revert() {
        val action = history.revert() ?: return
        when {
            action.changesCurrentPlayer -> {
                val ids = action.currentPlayerIds ?: return
                mCurrentPlayerId.value = ids.prev
            }
            action.changesPlayerState -> {
                val states = action.playerStates ?: return
                updatePlayerState(action, states.prev)
            }
        }
    }

    final override fun repeat() {
        val action = history.repeat() ?: return
        when {
            action.changesCurrentPlayer -> {
                val ids = action.currentPlayerIds ?: return
                mCurrentPlayerId.value = ids.next
            }
            action.changesPlayerState -> {
                val states = action.playerStates ?: return
                updatePlayerState(action, states.next)
            }
        }
    }

    private fun updatePlayerState(action: GameAction, state: PlayerState) {
        val id = action.playerId ?: return
        mPlayers.updateMap {
            val player = get(id) ?: return
            val updPlayer = player.copy(state = state)
            put(id, updPlayer)
        }
    }

    open val initializables: Array<Initializable> = arrayOf(timer)

    fun load(session: GameSession): Unit = session.let {
        mCompleted.value = it.completed
        name.value = it.name
        it.players.let { ps ->
            mPlayers.value = ps.toMap()
            playersIds = ps.map { p -> p.first }
        }
        mCurrentPlayerId.value = it.currentPlayerId
        nextNewPlayerId.set(it.nextNewPlayerId)
        startTime = it.startTime
        stopTime = it.stopTime
        timeout.value = it.timeout
        mSecondsToEnd.value = it.secondsUntilEnd
        history.setup(it.actions, it.currentActionPosition)
    }

    fun save() = GameSession(
        completed = completed.value,
        type = type,
        name = name.value,
        players = players.value.let { ps ->
            playersIds.map { it to ps[it]!! }
        },
        currentPlayerId = currentPlayerId.value,
        nextNewPlayerId = nextNewPlayerId.get(),
        startTime = startTime,
        stopTime = stopTime,
        timeout = timeout.value,
        secondsUntilEnd = secondsToEnd.value,
        actions = history.actionsContainer,
        currentActionPosition = history.nextActionIndex
    )

    protected open var playersIds: List<Long>
        get() = players.value.keys.toList()
        set(_) {}
}

private const val TAG = "GameEnvironment"
