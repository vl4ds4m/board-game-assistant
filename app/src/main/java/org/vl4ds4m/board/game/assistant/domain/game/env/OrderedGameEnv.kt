package org.vl4ds4m.board.game.assistant.domain.game.env

import org.vl4ds4m.board.game.assistant.domain.Player

interface OrderedGameEnv : GameEnv, Ordered {
    val currentPlayer: Player?
        get() = order.value?.let { players.value[it] }
}
