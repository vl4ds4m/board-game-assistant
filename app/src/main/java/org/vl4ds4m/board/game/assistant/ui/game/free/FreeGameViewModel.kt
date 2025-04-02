package org.vl4ds4m.board.game.assistant.ui.game.free

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class FreeGameViewModel private constructor(
    private val gameEnv: FreeGame = FreeGame(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : GameViewModel(gameEnv, sessionId, app) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelProducer<FreeGameViewModel> {
        override fun createViewModel(game: Game, app: BoardGameAssistantApp) =
            FreeGameViewModel(gameEnv = game as FreeGame, app = app)

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            FreeGameViewModel(sessionId = sessionId, app = app)
    }
}
