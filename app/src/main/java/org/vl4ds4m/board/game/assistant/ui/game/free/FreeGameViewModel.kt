package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.lifecycle.viewmodel.CreationExtras
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class FreeGameViewModel private constructor(
    private val gameEnv: FreeGame = FreeGame(),
    sessionId: Long? = null,
    extras: CreationExtras
) : GameViewModel(gameEnv, sessionId, extras) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelProducer<FreeGameViewModel> {
        override fun createViewModel(game: Game, extras: CreationExtras) =
            FreeGameViewModel(
                gameEnv = game as FreeGame,
                extras = extras
            )

        override fun createViewModel(sessionId: Long, extras: CreationExtras) =
            FreeGameViewModel(
                sessionId = sessionId,
                extras = extras
            )
    }
}
