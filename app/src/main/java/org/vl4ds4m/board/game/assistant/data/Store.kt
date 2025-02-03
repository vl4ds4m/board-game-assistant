package org.vl4ds4m.board.game.assistant.data

import androidx.compose.runtime.mutableStateListOf
import org.vl4ds4m.board.game.assistant.domain.game.GameType

object Store {
    private val _sessions = mutableStateListOf<Session>()
    private var nextId: Long = 1

    init {
        defaultGames.forEach { (name, type) ->
            addSession(name, type, listOf())
        }
    }

    val sessions: List<Session> = _sessions

    fun addSession(
        name: String,
        type: GameType,
        players: List<String>
    ) {
        val newSession = Session(
            id = nextId,
            name = name,
            type = type,
            players = players
        )
        nextId++
        _sessions.add(0, newSession)
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
