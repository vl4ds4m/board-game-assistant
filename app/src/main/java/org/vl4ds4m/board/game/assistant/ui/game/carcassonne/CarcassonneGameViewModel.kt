package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.CarcassonneGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

class CarcassonneGameViewModel(
    game: CarcassonneGame = CarcassonneGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    override fun addPoints(points: Int) {}

    companion object : GameViewModelFactory<CarcassonneGameViewModel> {
        override fun create(sessionId: Long?): CarcassonneGameViewModel {
            return if (sessionId == null) {
                val game = Store.currentGame as CarcassonneGame
                CarcassonneGameViewModel(game)
            } else {
                CarcassonneGameViewModel(sessionId = sessionId)
            }
        }
    }
}
