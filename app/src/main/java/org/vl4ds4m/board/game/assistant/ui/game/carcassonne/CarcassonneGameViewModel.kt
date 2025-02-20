package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneProperty
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel

class CarcassonneGameViewModel(
    private val gameEnv: CarcassonneGame = CarcassonneGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(gameEnv, sessionId) {
    val onFinal: MutableStateFlow<Boolean> = gameEnv.onFinal

    fun addPoints(property: CarcassonneProperty, count: Int) {
        gameEnv.addPoints(property, count)
    }

    fun skip() {
        gameEnv.changeCurrentPlayerId()
    }

    companion object : GameViewModelFactory {
        override fun createFrom(gameEnv: GameEnv): CarcassonneGameViewModel {
            return CarcassonneGameViewModel(gameEnv = gameEnv as CarcassonneGame)
        }

        override fun createFrom(sessionId: Long): CarcassonneGameViewModel {
            return CarcassonneGameViewModel(sessionId = sessionId)
        }
    }
}
