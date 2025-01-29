package org.vl4ds4m.board.game.assistant.data

import org.vl4ds4m.board.game.assistant.data.model.Session
import java.time.Instant

class Store {
    companion object {
        val sessions: MutableList<Session> = ArrayList(defaultSessions)
    }
}

private val initialTime = Instant.parse("2025-01-24T10:15:34.00Z")
    .epochSecond

private val defaultSessions = listOf(
    Session(
        type = GameType.ORDERED,
        name = "Imaginarium",
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 15),
        stopTime = Instant.ofEpochSecond(initialTime + 136),
        completed = false
    ),
    Session(
        type = GameType.MONOPOLY,
        name = GameType.MONOPOLY.title,
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 87),
        stopTime = Instant.ofEpochSecond(initialTime + 2564),
        completed = false
    ),
    Session(
        type = GameType.FREE,
        name = "Poker Counts",
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 7342),
        stopTime = Instant.ofEpochSecond(initialTime + 9645),
        completed = false
    ),
    Session(
        type = GameType.ORDERED,
        name = "Uno",
        players = listOf(),
        startTime = Instant.ofEpochSecond(initialTime + 100),
        stopTime = Instant.ofEpochSecond(initialTime + 120),
        completed = false
    ),
)
