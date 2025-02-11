package org.vl4ds4m.board.game.assistant.data

import androidx.compose.runtime.mutableStateMapOf
import org.vl4ds4m.board.game.assistant.domain.game.Game
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import java.util.concurrent.atomic.AtomicLong

object Store {
    private val mSessions = mutableStateMapOf<Long, GameSession>()
    private var nextSessionId: AtomicLong = AtomicLong(0)

    init {
        defaultGames.forEach { (name, type) ->
            val session = GameSession(type = type, name = name, fake = true)
            save(session)
        }
    }

    var currentGame: Game<*>? = null

    val sessions: Map<Long, GameSession> = mSessions

    fun save(session: GameSession, id: Long? = null){
        val sessionId: Long = when (id) {
            null -> {
                this.nextSessionId.incrementAndGet()
            }
            in mSessions -> { id }
            else -> { return }
        }
        mSessions[sessionId] = session
    }

    fun load(sessionId: Long): GameSession? {
        return sessions[sessionId]?.takeUnless { it.fake }
    }
}

private val defaultGames: List<Pair<String, GameType>> = listOf(
    "Imaginarium" to GameType.ORDERED,
    GameType.MONOPOLY.title to GameType.MONOPOLY,
    "Poker Counts" to GameType.FREE,
    GameType.DICE.title to GameType.DICE,
    "Uno" to GameType.ORDERED,
    GameType.CARCASSONNE.title to GameType.CARCASSONNE
)

/*private val initialTime = Instant.parse("2025-01-24T10:15:34.00Z")
    .epochSecond*/

/*startTime = Instant.ofEpochSecond(initialTime + 15),
stopTime = Instant.ofEpochSecond(initialTime + 136),
completed = false*/

/*startTime = Instant.ofEpochSecond(initialTime + 87),
stopTime = Instant.ofEpochSecond(initialTime + 2564),
completed = false*/

/*startTime = Instant.ofEpochSecond(initialTime + 7342),
stopTime = Instant.ofEpochSecond(initialTime + 9645),
completed = false*/

/*startTime = Instant.ofEpochSecond(initialTime + 176),
stopTime = Instant.ofEpochSecond(initialTime + 89434),
completed = false*/

/*startTime = Instant.ofEpochSecond(initialTime + 100),
stopTime = Instant.ofEpochSecond(initialTime + 120),
completed = false*/

/*startTime = Instant.ofEpochSecond(initialTime + 2345),
stopTime = Instant.ofEpochSecond(initialTime + 8438),
completed = false*/
