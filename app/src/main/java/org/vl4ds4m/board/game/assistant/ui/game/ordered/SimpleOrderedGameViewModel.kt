package org.vl4ds4m.board.game.assistant.ui.game.ordered

import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class SimpleOrderedGameViewModel private constructor(
    private val gameEnv: SimpleOrderedGame = SimpleOrderedGame(),
    sessionId: Long? = null,
    sessionRepository: GameSessionRepository
) : OrderedGameViewModel(gameEnv, sessionId, sessionRepository) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelProducer<SimpleOrderedGameViewModel> {
        override fun createViewModel(game: Game, sessionRepository: GameSessionRepository) =
            SimpleOrderedGameViewModel(
                gameEnv = game as SimpleOrderedGame,
                sessionRepository = sessionRepository
            )

        override fun createViewModel(sessionId: Long, sessionRepository: GameSessionRepository) =
            SimpleOrderedGameViewModel(
                sessionId = sessionId,
                sessionRepository = sessionRepository
            )
    }
}
