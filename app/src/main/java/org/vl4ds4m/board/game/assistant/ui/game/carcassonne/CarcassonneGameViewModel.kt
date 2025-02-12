package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import android.util.Log
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

class CarcassonneGameViewModel(
    game: CarcassonneGame = CarcassonneGame(),
    sessionId: Long? = null
) : OrderedGameViewModel(
    game = game,
    sessionId = sessionId
) {
    override fun addPoints(points: Int) {
        if (points <= 0) {
            Log.i(TAG, "Skip")
        } else {
            Log.i(TAG, "Add $points point(s)")
        }
    }

    companion object : GameViewModelFactory<CarcassonneGameViewModel> {
        override fun create(sessionId: Long?): CarcassonneGameViewModel {
            return if (sessionId == null) {
                val game = Store.currentGame as CarcassonneGame
                CarcassonneGameViewModel(game)
            } else {
                CarcassonneGameViewModel(sessionId = sessionId)
            }
        }
    }
}

private const val TAG = "Carcassonne"
