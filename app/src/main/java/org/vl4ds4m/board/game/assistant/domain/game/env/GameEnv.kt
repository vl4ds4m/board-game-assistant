package org.vl4ds4m.board.game.assistant.domain.game.env

import org.vl4ds4m.board.game.assistant.domain.player.Player

interface GameEnv {
    var name: String?

    fun getPlayers(): List<Player>

    fun addPlayer(newPlayer: Player)

    fun removePlayer(player: Player)

    fun freezePlayer(player: Player)

    fun unfreezePlayer(player: Player)

    fun completeGame()
}
