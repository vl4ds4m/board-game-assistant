package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.StateFlow

interface OrderedGameEnv : GameEnv {
    val orderedPlayerIds: StateFlow<List<Long>>

    fun selectNextPlayerId()

    fun changePlayerOrder(id: Long, order: Int)
}
