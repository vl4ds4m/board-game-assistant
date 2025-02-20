package org.vl4ds4m.board.game.assistant.ui.game.dice

import org.vl4ds4m.board.game.assistant.game.DiceGame
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel

class DiceGameViewModel private constructor(
    private val gameEnv: DiceGame = DiceGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(gameEnv, sessionId) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): DiceGameViewModel {
            return DiceGameViewModel(gameEnv = gameEnv as DiceGame)
        }

        override fun createFrom(sessionId: Long): DiceGameViewModel {
            return DiceGameViewModel(sessionId = sessionId)
        }
    }
}
