package org.vl4ds4m.board.game.assistant.data

import androidx.compose.runtime.mutableStateMapOf
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.state.Score
import java.util.concurrent.atomic.AtomicLong

object Store {
    private val mSessions = mutableStateMapOf<Long, GameSession>()
    private var nextSessionId: AtomicLong = AtomicLong(0)

    init {
        defaultGames.forEach { save(it) }
    }

    var currentGameEnv: GameEnv? = null

    val sessions: Map<Long, GameSession> = mSessions

    fun save(session: GameSession, id: Long? = null) {
        val sessionId = when (id) {
            null -> this.nextSessionId.incrementAndGet()
            in mSessions -> id
            else -> return
        }
        mSessions[sessionId] = session
    }

    fun load(sessionId: Long): GameSession? {
        return sessions[sessionId]
    }
}

private val initialTime: Long = java.time.Instant
    .parse("2025-01-24T10:15:34.00Z").epochSecond

private val defaultGames: List<GameSession> = listOf(
    GameSession(
        type = SimpleOrdered,
        name = "Uno 93",
        players = mapOf(
            1L to Player(
                name = "Abc",
                active = true,
                state = Score(120)
            ),
            2L to Player(
                name = "Def",
                active = false,
                state = Score(36)
            ),
            3L to Player(
                name = "Foo",
                active = true,
                state = Score(154)
            )
        ),
        currentPlayerId = 1L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 40_000,
    ),
    GameSession(
        type = Free,
        name = "Poker Counts 93",
        players = mapOf(
            1L to Player(
                name = "Bar",
                active = true,
                state = Score(1220)
            ),
            2L to Player(
                name = "Conf",
                active = true,
                state = Score(376)
            ),
            3L to Player(
                name = "Leak",
                active = true,
                state = Score(532)
            )
        ),
        currentPlayerId = 2L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 20_000,
    ),
    GameSession(
        type = SimpleOrdered,
        completed = true,
        name = "Imaginarium 74",
        players = mapOf(
            1L to Player(
                name = "Bar",
                active = true,
                state = Score(12)
            ),
            2L to Player(
                name = "Conf",
                active = true,
                state = Score(37)
            ),
            3L to Player(
                name = "Leak",
                active = true,
                state = Score(53)
            ),
            4L to Player(
                name = "Flick",
                active = true,
                state = Score(32)
            )
        ),
        currentPlayerId = 3L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 30_000
    )
)
