package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.lifecycle.viewmodel.CreationExtras
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class MonopolyGameViewModel(
    gameEnv: MonopolyGame = MonopolyGame(),
    sessionId: String? = null,
    extras: CreationExtras
) : OrderedGameViewModel(gameEnv, sessionId, extras) {
    companion object : GameViewModelProducer<MonopolyGameViewModel> {
        override fun createViewModel(game: Game, extras: CreationExtras) =
            MonopolyGameViewModel(
                gameEnv = game as MonopolyGame,
                extras = extras
            )

        override fun createViewModel(sessionId: String, extras: CreationExtras) =
            MonopolyGameViewModel(
                sessionId = sessionId,
                extras = extras
            )
    }
}
