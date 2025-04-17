package org.vl4ds4m.board.game.assistant.ui.game.simple

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGame
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGameEnv
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModelProducer

class SimpleOrderedGameViewModel private constructor(
    override val gameEnv: SimpleOrderedGameEnv = SimpleOrderedGameEnv(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : GameViewModel(gameEnv, sessionId, app), SimpleOrderedGame by gameEnv {
    companion object : GameViewModelProducer<SimpleOrderedGameViewModel> {
        override fun createViewModel(gameEnv: GameEnv, app: BoardGameAssistantApp) =
            SimpleOrderedGameViewModel(
                gameEnv = gameEnv as SimpleOrderedGameEnv,
                app = app
            )

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            SimpleOrderedGameViewModel(
                sessionId = sessionId,
                app = app
            )
    }
}
