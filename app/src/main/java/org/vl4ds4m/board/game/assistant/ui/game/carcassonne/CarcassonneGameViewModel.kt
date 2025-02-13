package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import android.util.Log
import org.vl4ds4m.board.game.assistant.domain.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

class CarcassonneGameViewModel(
    game: CarcassonneGame = CarcassonneGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    override val name: String = "Carcassonne '${game.name.value}'"

    override fun addPoints(points: Int) {
        if (points <= 0) {
            Log.i(TAG, "Skip")
        } else {
            Log.i(TAG, "Add $points point(s)")
        }
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

private const val TAG = "Carcassonne"
