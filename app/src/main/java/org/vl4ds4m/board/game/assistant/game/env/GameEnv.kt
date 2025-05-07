package org.vl4ds4m.board.game.assistant.game.env

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.vl4ds4m.board.game.assistant.States
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.OrderedPlayers
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.Users
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.changesCurrentPlayer
import org.vl4ds4m.board.game.assistant.game.changesPlayerState
import org.vl4ds4m.board.game.assistant.game.currentPlayerChangedAction
import org.vl4ds4m.board.game.assistant.game.currentPIDs
import org.vl4ds4m.board.game.assistant.game.log.GameActionsHistory
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.playerId
import org.vl4ds4m.board.game.assistant.game.playerStateChangedAction
import org.vl4ds4m.board.game.assistant.game.playerStates
import org.vl4ds4m.board.game.assistant.updateAndGetStates
import org.vl4ds4m.board.game.assistant.updateMap
import java.util.concurrent.atomic.AtomicInteger

open class GameEnv(final override val type: GameType) : Game {
    final override val name: MutableStateFlow<String> = MutableStateFlow("")

    protected val mPlayers: MutableStateFlow<Players> = MutableStateFlow(mapOf())
    final override val players: StateFlow<Players> = mPlayers.asStateFlow()

    protected val mOrderedPlayers = MutableStateFlow<OrderedPlayers>(listOf())
    final override val orderedPlayers: StateFlow<OrderedPlayers> =
        mOrderedPlayers.asStateFlow()

    protected open val orderedPlayersUpdater = Initializable { scope ->
        players.onEach {
            mOrderedPlayers.value = it.toList()
        }.launchIn(scope)
    }

    private val mUsers = MutableStateFlow<Users>(mapOf())
    final override val users: StateFlow<Users> = mUsers.asStateFlow()

    protected val mCurrentPid: MutableStateFlow<PID?> = MutableStateFlow(null)
    final override val currentPid: StateFlow<PID?> = mCurrentPid.asStateFlow()

    protected val history = GameActionsHistory()

    final override fun changeCurrentPid(id: PID) {
        players.value[id]
            ?.takeIf { it.active }
            ?: return
        val ids = mCurrentPid.updateAndGetStates { id }
        addCurrentPlayerChangedAction(ids)
    }

    protected fun addCurrentPlayerChangedAction(ids: States<PID?>) {
        if (ids.prev == ids.next) return
        history += currentPlayerChangedAction(ids)
    }

    override fun addPlayer(user: User?, name: String): PID =
        addPlayer(user, name, PlayerState(0, mapOf()))

    private val nextNewPid: AtomicInteger = AtomicInteger(0)

    protected open fun addPlayer(user: User?, name: String, state: PlayerState): PID {
        val id = nextNewPid.incrementAndGet()
        val player = Player(
            name = name,
            state = state
        )
        mPlayers.updateMap { put(id, player) }
        if (user != null) {
            mUsers.updateMap { put(id, user) }
        }
        val ids = mCurrentPid.updateAndGetStates { it ?: id }
        if (initialized.value) {
            addCurrentPlayerChangedAction(ids)
        }
        return id
    }

    override fun removePlayer(id: PID) {
        val (players, _) = remove(id) ?: return
        val ids = updateCurrentIdOnEqual(id, players) ?: return
        addCurrentPlayerChangedAction(ids)
    }

    protected fun remove(id: PID): States<Players>? {
        val states = mPlayers.updateMap {
            val player = get(id) ?: return null
            val newPlayer = player.copy(presence = Player.Presence.REMOVED)
            put(id, newPlayer)
        }
        mUsers.updateMap { remove(id) }
        return states
    }

    private fun updateCurrentIdOnEqual(
        pid: PID,
        players: Players
    ): States<PID?>? = mCurrentPid.updateAndGetStates { currentId ->
        if (currentId != pid) return null
        players.firstNotNullOfOrNull { (id, player) ->
            id.takeIf { player.active && id != pid }
        }
    }

    final override fun bindPlayer(id: PID, user: User) {
        mPlayers.value[id]?.takeIf { !it.removed } ?: return
        mUsers.updateMap { putIfAbsent(id, user) }
    }

    final override fun unbindPlayer(id: PID) {
        mUsers.updateMap { remove(id) }
    }

    final override fun renamePlayer(id: PID, name: String) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            if (player.name == name) return
            put(id, player.copy(name = name))
        }
    }

    override fun freezePlayer(id: PID) {
        val (players, _) = freeze(id) ?: return
        val ids = updateCurrentIdOnEqual(id, players) ?: return
        addCurrentPlayerChangedAction(ids)
    }

    protected fun freeze(pid: PID): States<Players>? {
        return mPlayers.updateMap {
            val player = get(pid) ?: return null
            if (!player.active) return null
            put(pid, player.copy(presence = Player.Presence.FROZEN))
        }
    }

    final override fun unfreezePlayer(id: PID) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            if (!player.frozen) return
            put(id, player.copy(presence = Player.Presence.ACTIVE))
        }
        val ids = mCurrentPid.updateAndGetStates { it ?: id }
        addCurrentPlayerChangedAction(ids)
    }

    final override fun changePlayerState(id: PID, state: PlayerState) {
        val (oldPlayers, newPlayers) = mPlayers.updateMap {
            val player = get(id)?.takeIf { it.active } ?: return
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
    private var lastStart: Long? = null
    private var duration: Long? = null

    final override val timeout = MutableStateFlow(false)

    private val mSecondsToEnd = MutableStateFlow(0)
    final override val secondsToEnd: StateFlow<Int> = mSecondsToEnd.asStateFlow()

    private val mInitialized = MutableStateFlow(false)
    final override val initialized: StateFlow<Boolean> = mInitialized.asStateFlow()

    private val mCompleted = MutableStateFlow(false)
    final override val completed: StateFlow<Boolean> = mCompleted.asStateFlow()

    final override fun changeSecondsToEnd(seconds: Int) {
        if (seconds >= 0) mSecondsToEnd.value = seconds
    }

    private val timer: Timer = Timer()

    final override fun initialize() {
        Log.i(TAG, "Initialize game")
        mInitialized.value = true
    }

    final override fun start() {
        Log.i(TAG, "Start game")
        val millis = System.currentTimeMillis()
        if (startTime == null) {
            startTime = millis
        }
        lastStart = millis
        timer.start(timeout, mSecondsToEnd, ::complete)
    }

    final override fun stop() {
        Log.i(TAG, "Stop game")
        val millis = System.currentTimeMillis()
        stopTime = millis
        lastStart?.let {
            duration = (millis - it) + (duration ?: 0)
        }
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
        revert(action)
    }

    open fun revert(action: GameAction) {
        when {
            action.changesCurrentPlayer -> {
                val ids = action.currentPIDs ?: return
                mCurrentPid.value = ids.prev
            }
            action.changesPlayerState -> {
                val states = action.playerStates ?: return
                updatePlayerState(action, states.prev)
            }
        }
    }

    final override fun repeat() {
        val action = history.repeat() ?: return
        repeat(action)
    }

    open fun repeat(action: GameAction) {
        when {
            action.changesCurrentPlayer -> {
                val ids = action.currentPIDs ?: return
                mCurrentPid.value = ids.next
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

    open val initializables: Array<Initializable>
        get() = arrayOf(timer, orderedPlayersUpdater)

    protected open var mPIDs: List<PID>
        get() = emptyPIDs
        set(_) {}

    fun load(session: GameSession): Unit = session.let {
        mCompleted.value = it.completed
        name.value = it.name
        it.players.let { ps ->
            mPlayers.value = ps.toMap()
            mPIDs = ps.map { (id, _) -> id }
        }
        mUsers.value = it.users
        mCurrentPid.value = it.currentPid
        nextNewPid.set(it.nextNewPid)
        startTime = it.startTime
        stopTime = it.stopTime
        duration = it.duration
        mSecondsToEnd.value = it.secondsUntilEnd
        timeout.value = it.timeout
        history.setup(it.actions, it.currentActionPosition)
    }

    fun save() = GameSession(
        completed = completed.value,
        type = type,
        name = name.value,
        players = Game.getOrderedPlayers(mPIDs, players.value),
        users = users.value,
        currentPid = currentPid.value,
        nextNewPid = nextNewPid.get(),
        startTime = startTime,
        stopTime = stopTime,
        duration = duration,
        timeout = timeout.value,
        secondsUntilEnd = secondsToEnd.value,
        actions = history.actionsContainer,
        currentActionPosition = history.nextActionIndex
    )
}

private const val TAG = "GameEnvironment"

private val emptyPIDs = listOf<PID>()
