package org.vl4ds4m.board.game.assistant.ui.game.dice

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.DiceGame
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class DiceGameViewModel private constructor(
    private val gameEnv: DiceGame = DiceGame(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : OrderedGameViewModel(gameEnv, sessionId, app) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelProducer<DiceGameViewModel> {
        override fun createViewModel(game: Game, app: BoardGameAssistantApp) =
            DiceGameViewModel(
                gameEnv = game as DiceGame,
                app = app
            )

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            DiceGameViewModel(
                sessionId = sessionId,
                app = app
            )
    }
}
