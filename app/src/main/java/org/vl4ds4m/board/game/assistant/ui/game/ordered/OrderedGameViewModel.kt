package org.vl4ds4m.board.game.assistant.ui.game.ordered

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGame
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel

class OrderedGameViewModel(
    private val game: OrderedGame = OrderedGame(),
    sessionId: Long? = null
) : GameViewModel(
    game = game,
    sessionId = sessionId
) {
    private val mCurrentPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val currentPlayerId: StateFlow<Long?> = mCurrentPlayerId.asStateFlow()

    init {
        launchCurrentPlayerIdUpdate()
    }

    private fun launchCurrentPlayerIdUpdate() {
        game.players.combine(game.order) { players, order -> players to order }
            .onEach { (players, order) ->
                mCurrentPlayerId.update {
                    order?.let { players[it].id }
                }
            }
            .launchIn(viewModelScope)
    }

    fun addScore(points: Int) {
        game.addPoints(points)
    }

    companion object {
        fun create(sessionId: Long?): OrderedGameViewModel {
            return if (sessionId == null) {
                val game = Store.currentGame as OrderedGame
                OrderedGameViewModel(game = game)
            } else {
                OrderedGameViewModel(sessionId = sessionId)
            }
        }
    }
}
