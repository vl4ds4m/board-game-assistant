package org.vl4ds4m.board.game.assistant.domain.game.env

import org.vl4ds4m.board.game.assistant.domain.player.Player

interface OrderedGameEnv : GameEnv {
    var order: Int

    fun changePlayerOrder(player: Player, newOrder: Int)
}
