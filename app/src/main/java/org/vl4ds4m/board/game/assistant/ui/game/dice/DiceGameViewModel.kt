package org.vl4ds4m.board.game.assistant.ui.game.dice

import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.DiceGame
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class DiceGameViewModel private constructor(
    private val gameEnv: DiceGame = DiceGame(),
    sessionId: Long? = null,
    sessionRepository: GameSessionRepository
) : OrderedGameViewModel(gameEnv, sessionId, sessionRepository) {
    fun addPoints(points: Int) {
        gameEnv.addPoints(points)
    }

    companion object : GameViewModelProducer<DiceGameViewModel> {
        override fun createViewModel(game: Game, sessionRepository: GameSessionRepository) =
            DiceGameViewModel(
                gameEnv = game as DiceGame,
                sessionRepository = sessionRepository
            )

        override fun createViewModel(sessionId: Long, sessionRepository: GameSessionRepository) =
            DiceGameViewModel(
                sessionId = sessionId,
                sessionRepository = sessionRepository
            )
    }
}
