package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class MonopolyGameViewModel(
    gameEnv: MonopolyGame = MonopolyGame(),
    sessionId: Long? = null,
    sessionRepository: GameSessionRepository
) : OrderedGameViewModel(gameEnv, sessionId, sessionRepository) {
    companion object : GameViewModelProducer<MonopolyGameViewModel> {
        override fun createViewModel(game: Game, sessionRepository: GameSessionRepository) =
            MonopolyGameViewModel(
                gameEnv = game as MonopolyGame,
                sessionRepository = sessionRepository
            )

        override fun createViewModel(sessionId: Long, sessionRepository: GameSessionRepository) =
            MonopolyGameViewModel(
                sessionId = sessionId,
                sessionRepository = sessionRepository
            )
    }
}
