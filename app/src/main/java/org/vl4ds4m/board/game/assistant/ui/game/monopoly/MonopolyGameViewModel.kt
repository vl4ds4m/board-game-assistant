package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import org.vl4ds4m.board.game.assistant.domain.game.MonopolyGame
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

class MonopolyGameViewModel(
    game: MonopolyGame = MonopolyGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    override val name: String = "Monopoly '${game.name.value}'"

    override fun addPoints(points: Int) {}

    companion object : GameViewModelFactory<MonopolyGameViewModel> {
        override fun createFrom(gameEnv: GameEnv): MonopolyGameViewModel {
            return MonopolyGameViewModel(game = gameEnv as MonopolyGame)
        }

        override fun createFrom(sessionId: Long): MonopolyGameViewModel {
            return MonopolyGameViewModel(sessionId = sessionId)
        }
    }
}
