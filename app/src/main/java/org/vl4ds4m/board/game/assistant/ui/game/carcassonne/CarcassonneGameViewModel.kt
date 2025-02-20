package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneProperty
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel

class CarcassonneGameViewModel(
    private val game: CarcassonneGame = CarcassonneGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    val onFinal: MutableStateFlow<Boolean> = game.onFinal

    fun addPoints(property: CarcassonneProperty, count: Int) {
        game.addPoints(property, count)
    }

    fun skip() {
        game.selectNextPlayerId()
    }

    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): CarcassonneGameViewModel {
            return CarcassonneGameViewModel(game = gameEnv as CarcassonneGame)
        }

        override fun createFrom(sessionId: Long): CarcassonneGameViewModel {
            return CarcassonneGameViewModel(sessionId = sessionId)
        }
    }
}
