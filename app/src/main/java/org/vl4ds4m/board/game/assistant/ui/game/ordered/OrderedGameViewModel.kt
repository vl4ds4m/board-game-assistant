package org.vl4ds4m.board.game.assistant.ui.game.ordered

import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

abstract class OrderedGameViewModel(
    private val game: OrderedGameEnv,
    sessionId: Long? = null
) : GameViewModel(
    game = game,
    sessionId = sessionId
) {
    val orderedPlayerIds: StateFlow<List<Long>> = game.orderedPlayerIds

    fun changePlayerOrder(id: Long, order: Int) {
        game.changePlayerOrder(id, order)
    }
}
