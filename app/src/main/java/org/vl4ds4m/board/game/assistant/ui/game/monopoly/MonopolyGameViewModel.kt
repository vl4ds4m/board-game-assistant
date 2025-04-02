package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class MonopolyGameViewModel(
    gameEnv: MonopolyGame = MonopolyGame(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : OrderedGameViewModel(gameEnv, sessionId, app) {
    companion object : GameViewModelProducer<MonopolyGameViewModel> {
        override fun createViewModel(game: Game, app: BoardGameAssistantApp) =
            MonopolyGameViewModel(
                gameEnv = game as MonopolyGame,
                app = app
            )

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            MonopolyGameViewModel(
                sessionId = sessionId,
                app = app
            )
    }
}
