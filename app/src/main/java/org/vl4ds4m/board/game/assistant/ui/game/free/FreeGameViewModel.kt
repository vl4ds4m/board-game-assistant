package org.vl4ds4m.board.game.assistant.ui.game.free

import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class FreeGameViewModel private constructor(
    private val gameEnv: FreeGame = FreeGame(),
    sessionId: Long? = null,
    sessionRepository: GameSessionRepository
) : GameViewModel(gameEnv, sessionId, sessionRepository) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelProducer<FreeGameViewModel> {
        override fun createViewModel(game: Game, sessionRepository: GameSessionRepository) =
            FreeGameViewModel(
                gameEnv = game as FreeGame,
                sessionRepository = sessionRepository
            )

        override fun createViewModel(sessionId: Long, sessionRepository: GameSessionRepository) =
            FreeGameViewModel(
                sessionId = sessionId,
                sessionRepository = sessionRepository
            )
    }
}
