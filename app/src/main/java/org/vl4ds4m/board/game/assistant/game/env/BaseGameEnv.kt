package org.vl4ds4m.board.game.assistant.game.env

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.log.CurrentPlayerChangeAction
import org.vl4ds4m.board.game.assistant.game.log.GameActionsHistory
import org.vl4ds4m.board.game.assistant.game.log.PlayerStateChangeAction
import org.vl4ds4m.board.game.assistant.game.state.PlayerState
import org.vl4ds4m.board.game.assistant.game.state.Score
import org.vl4ds4m.board.game.assistant.util.updateAndGetStates
import org.vl4ds4m.board.game.assistant.util.updateMap
import java.util.concurrent.atomic.AtomicLong

typealias States<T> = Pair<T, T>

open class BaseGameEnv(override val type: GameType) : GameEnv {
    override val name: MutableStateFlow<String> = MutableStateFlow("")

    private val mPlayers: MutableStateFlow<Players> = MutableStateFlow(mapOf())
    override val players: StateFlow<Players> = mPlayers.asStateFlow()

    protected val mCurrentPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    override val currentPlayerId: StateFlow<Long?> = mCurrentPlayerId.asStateFlow()

    private val actionsHistory = GameActionsHistory()

    override fun changeCurrentPlayerId(id: Long) {
        players.value[id]?.takeIf { it.active } ?: return
        mCurrentPlayerId.updateAndGetStates { currentId ->
            if (currentId == id) return
            id
        }.let {
            addActionForCurrentIdUpdate(it)
        }
    }

    protected fun addActionForCurrentIdUpdate(states: States<Long?>) {
        val (old, new) = states
        actionsHistory += CurrentPlayerChangeAction(
            oldPlayerId = old,
            newPlayerId = new
        )
    }

    override fun addPlayer(name: String) {
        addPlayer(name, Score())
    }

    private val nextNewPlayerId: AtomicLong = AtomicLong(0)

    protected open fun addPlayer(name: String, state: PlayerState): Long {
        val id = nextNewPlayerId.incrementAndGet()
        val player = Player(
            name = name,
            active = true,
            state = state
        )
        mPlayers.updateMap { put(id, player) }
        mCurrentPlayerId.updateAndGetStates {
            it?.run { return id } ?: id
        }.let {
            addActionForCurrentIdUpdate(it)
        }
        return id
    }

    override fun removePlayer(id: Long) {
        val (players, _) = remove(id) ?: return
        updateCurrentIdOnEqual(id, players)?.let {
            addActionForCurrentIdUpdate(it)
        }
    }

    protected fun remove(id: Long): States<Players>? {
        return mPlayers.updateMap {
            remove(id) ?: return null
        }
    }

    private fun updateCurrentIdOnEqual(playerId: Long, players: Players): States<Long?>? {
        return mCurrentPlayerId.updateAndGetStates upd@{
            if (it != playerId) return null
            players.forEach { (id, player) ->
                if (player.active && id != playerId) return@upd id
            }
            return@upd null
        }
    }

    override fun renamePlayer(id: Long, name: String) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            if (player.name == name) return
            put(id, player.copy(name = name))
        }
    }

    override fun freezePlayer(id: Long) {
        val (players, _) = freeze(id) ?: return
        updateCurrentIdOnEqual(id, players)?.let {
            addActionForCurrentIdUpdate(it)
        }
    }

    protected fun freeze(playerId: Long): States<Players>? {
        return mPlayers.updateMap {
            val player = get(playerId) ?: return null
            if (!player.active) return null
            put(playerId, player.copy(active = false))
        }
    }

    override fun unfreezePlayer(id: Long) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            if (player.active) return
            put(id, player.copy(active = true))
        }
        mCurrentPlayerId.updateAndGetStates {
            it?.run { return } ?: id
        }.let {
            addActionForCurrentIdUpdate(it)
        }
    }

    override fun changePlayerState(id: Long, state: PlayerState) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            if (player.state == state) return
            put(id, player.copy(state = state))
        }.let {
            val (old, new) = it.run {
                first[id]!!.state to second[id]!!.state
            }
            actionsHistory += PlayerStateChangeAction(
                playerId = id,
                oldState = old,
                newState = new,
            )
        }
    }

    private var startTime: Long? = null

    override val timeout: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val mSecondsToEnd: MutableStateFlow<Int> = MutableStateFlow(0)
    override val secondsToEnd: StateFlow<Int> = mSecondsToEnd.asStateFlow()

    private val mCompleted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val completed: StateFlow<Boolean> = mCompleted.asStateFlow()

    override fun changeSecondsToEnd(seconds: Int) {
        if (seconds > 0) mSecondsToEnd.value = seconds
    }

    private val timer: Timer = Timer()

    override fun start() {
        Log.i(GameEnv.TAG, "Start game")
        if (startTime == null) {
            startTime = System.currentTimeMillis()
        }
        if (timeout.value) {
            timer.start(mSecondsToEnd, mCompleted)
        }
    }

    override fun stop() {
        Log.i(GameEnv.TAG, "Stop game")
        timer.stop()
    }

    override fun complete() {
        Log.i(GameEnv.TAG, "Complete game")
        mCompleted.value = true
    }

    override fun returnGame() {
        Log.i(GameEnv.TAG, "Return to completed game")
        timeout.value = false
        mCompleted.value = false
    }

    override val reverted: StateFlow<Boolean> = actionsHistory.reverted

    override val repeatable: StateFlow<Boolean> = actionsHistory.repeatable

    override val actions = actionsHistory.actions

    override fun revert() {
        val action = actionsHistory.revert() ?: return
        when (action) {
            is CurrentPlayerChangeAction -> {
                mCurrentPlayerId.value = action.oldPlayerId
            }
            is PlayerStateChangeAction -> {
                mPlayers.updateMap {
                    compute(action.playerId) { _, v ->
                        v?.copy(state = action.oldState)
                    }
                }
            }
        }
    }

    override fun repeat() {
        val action = actionsHistory.repeat() ?: return
        when (action) {
            is CurrentPlayerChangeAction -> {
                mCurrentPlayerId.value = action.newPlayerId
            }
            is PlayerStateChangeAction -> {
                mPlayers.updateMap {
                    compute(action.playerId) { _, v ->
                        v?.copy(state = action.newState)
                    }
                }
            }
        }
    }

    override val initializables: Array<Initializable> = arrayOf(timer)

    override fun loadFrom(session: GameSession) {
        session.let { s ->
            mCompleted.value = s.completed
            name.value = s.name
            mPlayers.value = s.players
            mCurrentPlayerId.value = s.currentPlayerId
            s.nextNewPlayerId?.let {
                nextNewPlayerId.set(it)
            }
            startTime = s.startTime
            timeout.value = s.timeout
            mSecondsToEnd.value = s.secondsToEnd
            actionsHistory.setup(s.actions, s.currentActionPosition)
        }
    }

    override fun saveIn(session: GameSession) {
        session.let {
            it.type = type
            it.completed = completed.value
            it.name = name.value
            it.players = players.value
            it.currentPlayerId = currentPlayerId.value
            it.nextNewPlayerId = nextNewPlayerId.get()
            it.startTime = startTime
            it.timeout = timeout.value
            it.secondsToEnd = secondsToEnd.value
            it.actions = actionsHistory.container
            it.currentActionPosition = actionsHistory.currentPosition
        }
    }
}
