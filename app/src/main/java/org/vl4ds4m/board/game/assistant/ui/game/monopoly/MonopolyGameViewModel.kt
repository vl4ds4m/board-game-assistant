package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.MonopolyGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

class MonopolyGameViewModel(
    game: MonopolyGame = MonopolyGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    override fun addPoints(points: Int) {}

    companion object : GameViewModelFactory<MonopolyGameViewModel> {
        override fun create(sessionId: Long?): MonopolyGameViewModel {
            return if (sessionId == null) {
                val game = Store.currentGame as MonopolyGame
                MonopolyGameViewModel(game)
            } else {
                MonopolyGameViewModel(sessionId = sessionId)
            }
        }
    }
}
