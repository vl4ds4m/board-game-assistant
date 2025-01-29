package org.vl4ds4m.board.game.assistant.data

import org.vl4ds4m.board.game.assistant.data.model.Session
import java.time.Instant

class Game {
    var type: GameType? = null
    var name: String? = null
    var startTime: Instant? = null

    val players: MutableList<Player> = mutableListOf()

    fun addPlayer(newPlayer: Player, index: Int) {
        players.add(index, newPlayer)
    }

    fun removePlayer(index: Int) {
        players.removeAt(index)
    }

    fun movePlayer(oldIndex: Int, newIndex: Int) {
        val player = players.removeAt(oldIndex)
        players.add(newIndex, player)
    }

    fun changeScore(playerIndex: Int, count: Int) {
        players[playerIndex].score += count
    }

    var order: Int = 0

    fun changeScore(count: Int) {
        players[order].score += count
        order = (order + 1) % players.size
    }

    fun save() {
        val session = Session(
            type = type!!,
            name = name!!,
            players = players,
            startTime = startTime!!,
            stopTime = Instant.now(),
            completed = false
        )
        Store.sessions.add(session)
    }

    fun restore(sessionIndex: Int) {
        val session = Store.sessions[sessionIndex]
        this.type = session.type
        this.name = session.name
        this.players.clear()
        this.players.addAll(session.players)
        this.startTime = session.startTime
    }
}
