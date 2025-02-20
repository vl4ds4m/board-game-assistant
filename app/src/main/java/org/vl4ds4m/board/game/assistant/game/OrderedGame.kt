package org.vl4ds4m.board.game.assistant.game

import kotlinx.coroutines.flow.StateFlow

interface OrderedGame : Game {
    val nextPlayerId: StateFlow<Long?>

    val orderedPlayerIds: StateFlow<List<Long>>

    fun changeCurrentPlayerId()

    fun changePlayerOrder(id: Long, order: Int)
}
