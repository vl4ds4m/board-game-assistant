package org.vl4ds4m.board.game.assistant.domain.game.env

import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.Score

abstract class BaseGameEnv : GameEnv {
    protected val playersStates = mutableMapOf<Player, Score>()

    // Must be used in a single thread
    private var nextPlayerId: Long = 0

    override var name: String? = null

    override fun addPlayer(newPlayer: Player) {
        newPlayer.id = nextPlayerId++
        playersStates[newPlayer] = Score(0)
    }

    override fun removePlayer(player: Player) {
        playersStates.remove(player)
    }

    override fun freezePlayer(player: Player) {
        player.active = false
    }

    override fun unfreezePlayer(player: Player) {
        player.active = true
    }

    /*fun save() {
        val session = Session(
            name = name!!,
            type = GameType.ORDERED,
            players = getPlayers(),
            startTime = Instant.now(),
            stopTime = Instant.now(),
            completed = false
        )
        Store.sessions.add(session)
    }

    fun restore(sessionIndex: Int) {
        val session = Store.sessions[sessionIndex]
        this.name = session.name
        this.players.clear()
        this.players.addAll(session.players)
    }*/
}
