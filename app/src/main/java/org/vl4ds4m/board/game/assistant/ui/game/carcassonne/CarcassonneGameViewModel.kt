package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneProperty
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class CarcassonneGameViewModel(
    private val gameEnv: CarcassonneGame = CarcassonneGame(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : OrderedGameViewModel(gameEnv, sessionId, app) {
    fun addPoints(property: CarcassonneProperty, count: Int, final: Boolean) {
        gameEnv.addPoints(property, count, final)
    }

    companion object : GameViewModelProducer<CarcassonneGameViewModel> {
        override fun createViewModel(game: Game, app: BoardGameAssistantApp) =
            CarcassonneGameViewModel(
                gameEnv = game as CarcassonneGame,
                app = app
            )

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            CarcassonneGameViewModel(
                sessionId = sessionId,
                app = app
            )
    }
}
