package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.Player
import org.vl4ds4m.board.game.assistant.util.updateMap
import java.util.concurrent.atomic.AtomicLong

open class BaseGameEnv(private val type: GameType) : GameEnv {
    override val name: MutableStateFlow<String> = MutableStateFlow("")

    private val mPlayers: MutableStateFlow<Map<Long, Player>> = MutableStateFlow(mapOf())
    override val players: StateFlow<Map<Long, Player>> = mPlayers.asStateFlow()

    private var nextPlayerId: AtomicLong = AtomicLong(0)

    override fun addPlayer(name: String): Long {
        val id = nextPlayerId.incrementAndGet()
        val player = Player(
            name = name,
            active = true,
            score = 0
        )
        mPlayers.updateMap { put(id, player) }
        return id
    }

    override fun removePlayer(id: Long) {
        mPlayers.updateMap { remove(id) }
    }

    override fun renamePlayer(id: Long, name: String) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            put(id, player.copy(name = name))
        }
    }

    override fun freezePlayer(id: Long) {
        updatePlayerActivity(id, false)
    }

    override fun unfreezePlayer(id: Long) {
        updatePlayerActivity(id, true)
    }

    private fun updatePlayerActivity(id: Long, active: Boolean) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            put(id, player.copy(active = active))
        }
    }

    override fun changePlayerScore(id: Long, score: Int) {
        mPlayers.updateMap {
            val player = get(id) ?: return
            put(id, player.copy(score = score))
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

    override val initializables: Array<Initializable> = arrayOf(timer)

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

    override fun loadFrom(session: GameSession) {
        session.let { s ->
            mCompleted.value = s.completed
            name.value = s.name
            mPlayers.value = s.players
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
            it.nextPlayerId = nextPlayerId.get()
            it.startTime = startTime
            it.timeout = timeout.value
            it.secondsToEnd = secondsToEnd.value
        }
    }
}
