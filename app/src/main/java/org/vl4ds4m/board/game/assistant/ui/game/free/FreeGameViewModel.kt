package org.vl4ds4m.board.game.assistant.ui.game.free

import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory

class FreeGameViewModel private constructor(
    private val gameEnv: FreeGame = FreeGame(),
    sessionId: Long? = null,
) : GameViewModel(gameEnv, sessionId) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): FreeGameViewModel {
            return FreeGameViewModel(gameEnv = gameEnv as FreeGame)
        }

        override fun createFrom(sessionId: Long): FreeGameViewModel {
            return FreeGameViewModel(sessionId = sessionId)
        }
    }
}
