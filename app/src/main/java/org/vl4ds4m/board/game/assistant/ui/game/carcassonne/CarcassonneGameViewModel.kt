package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneProperty
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelProducer

class CarcassonneGameViewModel(
    private val gameEnv: CarcassonneGame = CarcassonneGame(),
    sessionId: Long? = null,
    sessionRepository: GameSessionRepository
) : OrderedGameViewModel(gameEnv, sessionId, sessionRepository) {
    val onFinal: MutableStateFlow<Boolean> = gameEnv.finalStage

    fun addPoints(property: CarcassonneProperty, count: Int) {
        gameEnv.addPoints(property, count)
    }

    fun skip() {
        gameEnv.changeCurrentPlayerId()
    }

    companion object : GameViewModelProducer<CarcassonneGameViewModel> {
        override fun createViewModel(game: Game, sessionRepository: GameSessionRepository) =
            CarcassonneGameViewModel(
                gameEnv = game as CarcassonneGame,
                sessionRepository = sessionRepository
            )

        override fun createViewModel(sessionId: Long, sessionRepository: GameSessionRepository) =
            CarcassonneGameViewModel(
                sessionId = sessionId,
                sessionRepository = sessionRepository
            )
    }
}
