package org.vl4ds4m.board.game.assistant.ui.game.dice

import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.DiceGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

class DiceGameViewModel private constructor(
    private val game: DiceGame = DiceGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    override fun addPoints(points: Int) {
        game.addPoints(points)
    }

    companion object : GameViewModelFactory<DiceGameViewModel> {
        override fun create(sessionId: Long?): DiceGameViewModel {
            return if (sessionId == null) {
                val game = Store.currentGame as DiceGame
                DiceGameViewModel(game = game)
            } else {
                DiceGameViewModel(sessionId = sessionId)
            }
        }
    }
}