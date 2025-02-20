package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory

class MonopolyGameViewModel(
    gameEnv: MonopolyGame = MonopolyGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(gameEnv, sessionId) {
    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): MonopolyGameViewModel {
            return MonopolyGameViewModel(gameEnv = gameEnv as MonopolyGame)
        }

        override fun createFrom(sessionId: Long): MonopolyGameViewModel {
            return MonopolyGameViewModel(sessionId = sessionId)
        }
    }
}
