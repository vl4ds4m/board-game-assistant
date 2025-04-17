package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGameEnv
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModelProducer

class CarcassonneGameViewModel(
    gameEnv: CarcassonneGameEnv = CarcassonneGameEnv(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : GameViewModel(gameEnv, sessionId, app), CarcassonneGame by gameEnv {
    companion object : GameViewModelProducer<CarcassonneGameViewModel> {
        override fun createViewModel(gameEnv: GameEnv, app: BoardGameAssistantApp) =
            CarcassonneGameViewModel(
                gameEnv = gameEnv as CarcassonneGameEnv,
                app = app
            )

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            CarcassonneGameViewModel(
                sessionId = sessionId,
                app = app
            )
    }
}
