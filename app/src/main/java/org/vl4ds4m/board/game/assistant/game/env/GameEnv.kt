package org.vl4ds4m.board.game.assistant.game.env

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.log.GameActionsHistory
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.data.Score
import org.vl4ds4m.board.game.assistant.updateAndGetStates
import org.vl4ds4m.board.game.assistant.updateMap
import java.util.concurrent.atomic.AtomicLong

typealias States<T> = Pair<T, T>

open class GameEnv(override val type: GameType) : Game {
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
        addActionForCurrentIdUpdate(ids)
    }

    protected fun addActionForCurrentIdUpdate(ids: States<Long?>) {
        /*val (old, new) = ids // TODO Implement
        if (old != new) {
            history += CurrentPlayerChangeAction(
                oldPlayerId = old,
                newPlayerId = new
            )
        }*/
    }

    override fun addPlayer(netDevId: String?, name: String) {
        addPlayer(netDevId, name, Score())
    }

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
            addActionForCurrentIdUpdate(ids)
        }
        return id
    }

    override fun removePlayer(id: Long) {
        val (players, _) = remove(id) ?: return
        val ids = updateCurrentIdOnEqual(id, players) ?: return
        addActionForCurrentIdUpdate(ids)
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
        addActionForCurrentIdUpdate(ids)
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
        addActionForCurrentIdUpdate(ids)
    }

    final override fun changePlayerState(id: Long, state: PlayerState) {
        val (oldPlayers, newPlayers) = mPlayers.updateMap {
            val player = get(id) ?: return
            if (player.state == state) return
            put(id, player.copy(state = state))
        }
        /*history += PlayerStateChangeAction( // TODO Implement
            playerId = id,
            oldState = oldPlayers[id]!!.state,
            newState = newPlayers[id]!!.state,
        )*/
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

    final override fun revert() { // TODO Implement
        /*val action = history.revert() ?: return
        when (action) {
            is CurrentPlayerChangeAction -> {
                mCurrentPlayerId.value = action.oldPlayerId
            }
            is PlayerStateChangeAction -> {
                mPlayers.updateMap {
                    compute(action.playerId) { _, v ->
                        v?.run {
                            copy(state = producePlayerState(state, action.oldState))
                        }
                    }
                }
            }
        }*/
    }

    protected open fun producePlayerState(
        source: PlayerState, provider: PlayerState
    ): PlayerState = provider

    final override fun repeat() { // TODO Implement
        /*val action = history.repeat() ?: return
        when (action) {
            is CurrentPlayerChangeAction -> {
                mCurrentPlayerId.value = action.newPlayerId
            }
            is PlayerStateChangeAction -> {
                mPlayers.updateMap {
                    compute(action.playerId) { _, v ->
                        v?.run {
                            copy(state = producePlayerState(state, action.newState))
                        }
                    }
                }
            }
        }*/
    }

    open val initializables: Array<Initializable> = arrayOf(timer)

    fun load(session: GameSession): Unit = session.let {
        mCompleted.value = it.completed
        name.value = it.name
        mPlayers.value = it.players
        playersIds = it.orderedPlayerIds
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
        players = players.value,
        orderedPlayerIds = playersIds,
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

    companion object {
        const val TAG = "GameEnvironment"
    }
}
