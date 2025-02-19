package org.vl4ds4m.board.game.assistant.ui.game.dice

import org.vl4ds4m.board.game.assistant.domain.game.DiceGame
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel

class DiceGameViewModel private constructor(
    private val game: DiceGame = DiceGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    fun addPoints(points: Int) {
        game.addPoints(points)
    }

    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): DiceGameViewModel {
            return DiceGameViewModel(game = gameEnv as DiceGame)
        }

        override fun createFrom(sessionId: Long): DiceGameViewModel {
            return DiceGameViewModel(sessionId = sessionId)
        }
    }
}
