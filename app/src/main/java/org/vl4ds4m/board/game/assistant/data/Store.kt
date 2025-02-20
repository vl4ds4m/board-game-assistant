package org.vl4ds4m.board.game.assistant.data

import androidx.compose.runtime.mutableStateMapOf
import org.vl4ds4m.board.game.assistant.game.Carcassonne
import org.vl4ds4m.board.game.assistant.game.Dice
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
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

    var currentGame: GameEnv? = null

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
    "Imaginarium" to SimpleOrdered,
    Monopoly.title to Monopoly,
    "Poker Counts" to Free,
    Dice.title to Dice,
    "Uno" to SimpleOrdered,
    Carcassonne.title to Carcassonne
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
