package org.vl4ds4m.board.game.assistant.ui.game.ordered

import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory

class SimpleOrderedGameViewModel private constructor(
    private val gameEnv: SimpleOrderedGame = SimpleOrderedGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(gameEnv, sessionId) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): SimpleOrderedGameViewModel {
            return SimpleOrderedGameViewModel(gameEnv = gameEnv as SimpleOrderedGame)
        }

        override fun createFrom(sessionId: Long): SimpleOrderedGameViewModel {
            return SimpleOrderedGameViewModel(sessionId = sessionId)
        }
    }
}
