package org.vl4ds4m.board.game.assistant.data

import org.vl4ds4m.board.game.assistant.domain.game.GameType

class Store {
    companion object {
        val sessions: MutableList<Session> = ArrayList(defaultSessions)
    }
}

/*private val initialTime = Instant.parse("2025-01-24T10:15:34.00Z")
    .epochSecond*/

private val defaultSessions = listOf(
    Session(
        name = "Imaginarium",
        /*type = GameType.ORDERED,
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 15),
        stopTime = Instant.ofEpochSecond(initialTime + 136),
        completed = false*/
    ),
    Session(
        name = GameType.MONOPOLY.title,
        /*type = GameType.MONOPOLY,
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 87),
        stopTime = Instant.ofEpochSecond(initialTime + 2564),
        completed = false*/
    ),
    Session(
        name = "Poker Counts",
        /*type = GameType.FREE,
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 7342),
        stopTime = Instant.ofEpochSecond(initialTime + 9645),
        completed = false*/
    ),
    Session(
        name = GameType.DICE.title,
        /*type = GameType.DICE,
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 176),
        stopTime = Instant.ofEpochSecond(initialTime + 89434),
        completed = false*/
    ),
    Session(
        name = "Uno",
        /*type = GameType.ORDERED,
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 100),
        stopTime = Instant.ofEpochSecond(initialTime + 120),
        completed = false*/
    ),
    Session(
        name = GameType.CARCASSONNE.title,
        /*type = GameType.CARCASSONNE,
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 2345),
        stopTime = Instant.ofEpochSecond(initialTime + 8438),
        completed = false*/
    )
)
