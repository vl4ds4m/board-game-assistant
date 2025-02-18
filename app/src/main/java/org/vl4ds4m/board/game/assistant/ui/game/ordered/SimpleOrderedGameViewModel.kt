package org.vl4ds4m.board.game.assistant.ui.game.ordered

import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
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
    fun addPoints(points: Int) {
        game.addPoints(points)
    }

    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): SimpleOrderedGameViewModel {
            return SimpleOrderedGameViewModel(game = gameEnv as SimpleOrderedGame)
        }

        override fun createFrom(sessionId: Long): SimpleOrderedGameViewModel {
            return SimpleOrderedGameViewModel(sessionId = sessionId)
        }
    }
}
