package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneProperty
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class CarcassonneGameViewModel(
    private val gameEnv: CarcassonneGame = CarcassonneGame(),
    sessionId: String? = null,
    extras: CreationExtras
) : OrderedGameViewModel(gameEnv, sessionId, extras) {
    val onFinal: MutableStateFlow<Boolean> = gameEnv.finalStage

    fun addPoints(property: CarcassonneProperty, count: Int) {
        gameEnv.addPoints(property, count)
    }

    fun skip() {
        gameEnv.changeCurrentPlayerId()
    }

    companion object : GameViewModelProducer<CarcassonneGameViewModel> {
        override fun createViewModel(game: Game, extras: CreationExtras) =
            CarcassonneGameViewModel(
                gameEnv = game as CarcassonneGame,
                extras = extras
            )

        override fun createViewModel(sessionId: String, extras: CreationExtras) =
            CarcassonneGameViewModel(
                sessionId = sessionId,
                extras = extras
            )
    }
}
