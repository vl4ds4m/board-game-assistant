package org.vl4ds4m.board.game.assistant.ui.game.dice

import androidx.lifecycle.viewmodel.CreationExtras
import org.vl4ds4m.board.game.assistant.game.DiceGame
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class DiceGameViewModel private constructor(
    private val gameEnv: DiceGame = DiceGame(),
    sessionId: String? = null,
    extras: CreationExtras
) : OrderedGameViewModel(gameEnv, sessionId, extras) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelProducer<DiceGameViewModel> {
        override fun createViewModel(game: Game, extras: CreationExtras) =
            DiceGameViewModel(
                gameEnv = game as DiceGame,
                extras = extras
            )

        override fun createViewModel(sessionId: String, extras: CreationExtras) =
            DiceGameViewModel(
                sessionId = sessionId,
                extras = extras
            )
    }
}
