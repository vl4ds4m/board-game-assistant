package org.vl4ds4m.board.game.assistant.ui.game.ordered

import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.simple.SimpleOrderedGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

class SimpleOrderedGameViewModel private constructor(
    private val game: SimpleOrderedGame = SimpleOrderedGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    override fun addPoints(points: Int) {
        game.addPoints(points)
    }

    companion object : GameViewModelFactory<SimpleOrderedGameViewModel> {
        override fun create(sessionId: Long?): SimpleOrderedGameViewModel {
            return if (sessionId == null) {
                val game = Store.currentGame as SimpleOrderedGame
                SimpleOrderedGameViewModel(game = game)
            } else {
                SimpleOrderedGameViewModel(sessionId = sessionId)
            }
        }
    }
}