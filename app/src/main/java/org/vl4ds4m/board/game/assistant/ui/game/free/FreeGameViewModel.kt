package org.vl4ds4m.board.game.assistant.ui.game.free

import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory

class FreeGameViewModel private constructor(
    private val game: FreeGame = FreeGame(),
    sessionId: Long? = null,
) : GameViewModel(
    game = game,
    sessionId = sessionId
) {
    fun addPoints(points: Int) {
        game.addPoints(points)
    }

    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): FreeGameViewModel {
            return FreeGameViewModel(game = gameEnv as FreeGame)
        }

        override fun createFrom(sessionId: Long): FreeGameViewModel {
            return FreeGameViewModel(sessionId = sessionId)
        }
    }
}
