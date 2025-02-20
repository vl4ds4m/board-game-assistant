package org.vl4ds4m.board.game.assistant.ui.game.ordered

import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

abstract class OrderedGameViewModel(
    private val gameEnv: OrderedGameEnv,
    sessionId: Long? = null
) : GameViewModel(gameEnv, sessionId), OrderedGame {
    override val nextPlayerId: StateFlow<Long?> = gameEnv.nextPlayerId

    override val orderedPlayerIds: StateFlow<List<Long>> = gameEnv.orderedPlayerIds

    override fun changePlayerOrder(id: Long, order: Int) = gameEnv.changePlayerOrder(id, order)

    override fun changeCurrentPlayerId() = gameEnv.changeCurrentPlayerId()
}
