package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class MonopolyGameViewModel(
    override val gameEnv: MonopolyGameEnv = MonopolyGameEnv(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : GameViewModel(gameEnv, sessionId, app), MonopolyGame by gameEnv {
    companion object : GameViewModelProducer<MonopolyGameViewModel> {
        override fun createViewModel(gameEnv: GameEnv, app: BoardGameAssistantApp) =
            MonopolyGameViewModel(
                gameEnv = gameEnv as MonopolyGameEnv,
                app = app
            )

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            MonopolyGameViewModel(
                sessionId = sessionId,
                app = app
            )
    }
}
