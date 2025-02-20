package org.vl4ds4m.board.game.assistant.game

import kotlinx.coroutines.flow.StateFlow

interface OrderedGame : Game {
    val orderedPlayerIds: StateFlow<List<Long>>

    fun selectNextPlayerId()

    fun changePlayerOrder(id: Long, order: Int)
}
