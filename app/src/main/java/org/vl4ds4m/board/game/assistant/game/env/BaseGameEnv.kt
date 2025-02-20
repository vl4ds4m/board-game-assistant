package org.vl4ds4m.board.game.assistant.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.state.PlayerState
import org.vl4ds4m.board.game.assistant.game.state.Score
import org.vl4ds4m.board.game.assistant.util.updateMap
import java.util.concurrent.atomic.AtomicLong

open class BaseGameEnv(override val type: GameType) : GameEnv {
    override val name: MutableStateFlow<String> = MutableStateFlow("")

    private val mPlayers: MutableStateFlow<Map<Long, Player>> = MutableStateFlow(mapOf())
    override val players: StateFlow<Map<Long, Player>> = mPlayers.asStateFlow()

    protected val mCurrentPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    override val currentPlayerId: StateFlow<Long?> = mCurrentPlayerId.asStateFlow()

    override fun changeCurrentPlayerId(id: Long) {
        val player = players.value[id] ?: return
        if (player.active) {
            mCurrentPlayerId.value = id
        }
    }

    private var nextPlayerId: AtomicLong = AtomicLong(0)

    override fun addPlayer(name: String, state: PlayerState): Long {
        val id = nextPlayerId.incrementAndGet()
        val player = Player(
            name = name,
            active = true,
            state = state
        )
        mPlayers.updateMap { put(id, player) }
        mCurrentPlayerId.update { it ?: id }
        return id
    }

    override fun addPlayer(name: String) {
        addPlayer(name, Score())
    }

    override fun removePlayer(id: Long) {
        mPlayers.updateMap { remove(id) }
        mCurrentPlayerId.update { currentId ->
            currentId.takeUnless { it == id }
        }
    }

    override fun renamePlayer(id: Long, name: String) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            put(id, player.copy(name = name))
        }
    }

    override fun freezePlayer(id: Long) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            if (!player.active) return
            put(id, player.copy(active = false))
            mCurrentPlayerId.update { currentId ->
                currentId.takeUnless { it == id }
            }
        }
    }

    override fun unfreezePlayer(id: Long) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            if (player.active) return
            put(id, player.copy(active = true))
            mCurrentPlayerId.update { it ?: id }
        }
    }

    override fun changePlayerState(id: Long, state: PlayerState) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            put(id, player.copy(state = state))
        }
    }

    private var startTime: Long? = null

    override val timeout: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val mSecondsToEnd: MutableStateFlow<Int> = MutableStateFlow(0)
    override val secondsToEnd: StateFlow<Int> = mSecondsToEnd.asStateFlow()

    private val mCompleted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val completed: StateFlow<Boolean> = mCompleted.asStateFlow()

    override fun changeSecondsToEnd(seconds: Int) {
        if (seconds > 0) {
            mSecondsToEnd.value = seconds
        }
    }

    private val timer: Timer = Timer()

    override fun start() {
        if (startTime == null) {
            startTime = System.currentTimeMillis()
        }
        if (timeout.value) {
            timer.start(mSecondsToEnd, mCompleted)
        }
    }

    override fun stop() {
        timer.stop()
    }

    override fun complete() {
        mCompleted.value = true
    }

    override fun returnGame() {
        timeout.value = false
        mCompleted.value = false
    }

    override val initializables: Array<Initializable> = arrayOf(timer)

    override fun loadFrom(session: GameSession) {
        session.let { s ->
            mCompleted.value = s.completed
            name.value = s.name
            mPlayers.value = s.players
            mCurrentPlayerId.value = s.currentPlayerId
            s.nextPlayerId?.let {
                nextPlayerId.set(it)
            }
            startTime = s.startTime
            timeout.value = s.timeout
            mSecondsToEnd.value = s.secondsToEnd
        }
    }

    override fun saveIn(session: GameSession) {
        session.let {
            it.type = type
            it.completed = completed.value
            it.name = name.value
            it.players = players.value
            it.currentPlayerId = currentPlayerId.value
            it.nextPlayerId = nextPlayerId.get()
            it.startTime = startTime
            it.timeout = timeout.value
            it.secondsToEnd = secondsToEnd.value
        }
    }
}
