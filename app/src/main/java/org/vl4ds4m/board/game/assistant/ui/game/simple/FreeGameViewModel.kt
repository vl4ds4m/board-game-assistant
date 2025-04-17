package org.vl4ds4m.board.game.assistant.ui.game.simple

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.game.simple.FreeGameEnv
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModelProducer

class FreeGameViewModel private constructor(
    gameEnv: FreeGameEnv = FreeGameEnv(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : GameViewModel(gameEnv, sessionId, app), FreeGame by gameEnv {
    companion object : GameViewModelProducer<FreeGameViewModel> {
        override fun createViewModel(gameEnv: GameEnv, app: BoardGameAssistantApp) =
            FreeGameViewModel(gameEnv = gameEnv as FreeGameEnv, app = app)

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            FreeGameViewModel(sessionId = sessionId, app = app)
    }
}
