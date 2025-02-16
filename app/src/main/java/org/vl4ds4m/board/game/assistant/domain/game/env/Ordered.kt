package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.domain.Player

interface Ordered {
    val order: StateFlow<Int?>

    fun nextOrder()

    fun changeOrderByPlayer(player: Player)

    fun changePlayerOrder(player: Player, order: Int)
}
