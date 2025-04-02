package org.vl4ds4m.board.game.assistant.ui.game.ordered

import androidx.lifecycle.viewmodel.CreationExtras
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class SimpleOrderedGameViewModel private constructor(
    private val gameEnv: SimpleOrderedGame = SimpleOrderedGame(),
    sessionId: String? = null,
    extras: CreationExtras
) : OrderedGameViewModel(gameEnv, sessionId, extras) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelProducer<SimpleOrderedGameViewModel> {
        override fun createViewModel(game: Game, extras: CreationExtras) =
            SimpleOrderedGameViewModel(
                gameEnv = game as SimpleOrderedGame,
                extras = extras
            )

        override fun createViewModel(sessionId: String, extras: CreationExtras) =
            SimpleOrderedGameViewModel(
                sessionId = sessionId,
                extras = extras
            )
    }
}
