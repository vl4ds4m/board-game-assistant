package org.vl4ds4m.board.game.assistant.ui.game.dice

import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.DiceGame
import org.vl4ds4m.board.game.assistant.game.DiceGameEnv
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModelProducer

class DiceGameViewModel private constructor(
    gameEnv: DiceGameEnv = DiceGameEnv(),
    sessionId: String? = null,
    app: BoardGameAssistantApp
) : GameViewModel(gameEnv, sessionId, app), DiceGame by gameEnv {
    companion object : GameViewModelProducer<DiceGameViewModel> {
        override fun createViewModel(gameEnv: GameEnv, app: BoardGameAssistantApp) =
            DiceGameViewModel(
                gameEnv = gameEnv as DiceGameEnv,
                app = app
            )

        override fun createViewModel(sessionId: String, app: BoardGameAssistantApp) =
            DiceGameViewModel(
                sessionId = sessionId,
                app = app
            )
    }
}
