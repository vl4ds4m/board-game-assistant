package org.vl4ds4m.board.game.assistant.ui.game.ordered

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class SimpleOrderedGameViewModel private constructor(
    private val gameEnv: SimpleOrderedGame = SimpleOrderedGame(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : OrderedGameViewModel(gameEnv, sessionId, app) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelProducer<SimpleOrderedGameViewModel> {
        override fun createViewModel(game: Game, app: BoardGameAssistantApp) =
            SimpleOrderedGameViewModel(
                gameEnv = game as SimpleOrderedGame,
                app = app
            )

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            SimpleOrderedGameViewModel(
                sessionId = sessionId,
                app = app
            )
    }
}
