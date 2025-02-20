package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel

class MonopolyGameViewModel(
    game: MonopolyGame = MonopolyGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): MonopolyGameViewModel {
            return MonopolyGameViewModel(game = gameEnv as MonopolyGame)
        }

        override fun createFrom(sessionId: Long): MonopolyGameViewModel {
            return MonopolyGameViewModel(sessionId = sessionId)
        }
    }
}
