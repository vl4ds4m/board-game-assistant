package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.FreeGame
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel

class FreeGameViewModel private constructor(
    private val game: FreeGame = FreeGame(),
    sessionId: Long? = null,
) : GameViewModel(
    game = game,
    sessionId = sessionId
) {
    private val mCurrentPlayerId: MutableState<Long?> = mutableStateOf(null)
    val currentPlayerId: State<Long?> = mCurrentPlayerId

    fun selectCurrentPlayer(player: Player) {
        mCurrentPlayerId.value = player.id
    }

    fun addScore(points: Int) {
        mCurrentPlayerId.value?.let {
            game.addPoints(it, points)
        }
    }

    companion object {
        fun create(sessionId: Long?): FreeGameViewModel {
            return if (sessionId == null) {
                val game = Store.currentGame as FreeGame
                FreeGameViewModel(game = game)
            } else {
                FreeGameViewModel(sessionId = sessionId)
            }
        }
    }
}
