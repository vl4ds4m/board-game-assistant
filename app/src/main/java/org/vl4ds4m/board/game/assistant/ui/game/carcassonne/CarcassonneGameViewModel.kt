package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.domain.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.domain.game.carcassonne.CarcassonneProperty
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

class CarcassonneGameViewModel(
    private val game: CarcassonneGame = CarcassonneGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    override val name: String = "Carcassonne '${game.name.value}'"

    val onFinal: MutableStateFlow<Boolean> = game.onFinal

    fun addPoints(property: CarcassonneProperty, count: Int) {
        game.addPoints(property, count)
    }

    fun skip() {
        game.nextOrder()
    }

    companion object : GameViewModelFactory<CarcassonneGameViewModel> {
        override fun createFrom(gameEnv: GameEnv): CarcassonneGameViewModel {
            return CarcassonneGameViewModel(game = gameEnv as CarcassonneGame)
        }

        override fun createFrom(sessionId: Long): CarcassonneGameViewModel {
            return CarcassonneGameViewModel(sessionId = sessionId)
        }
    }
}
