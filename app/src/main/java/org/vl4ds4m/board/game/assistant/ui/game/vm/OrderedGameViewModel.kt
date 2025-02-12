package org.vl4ds4m.board.game.assistant.ui.game.vm

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGame

abstract class OrderedGameViewModel(
    game: OrderedGame,
    sessionId: Long? = null
) : GameViewModel(
    game = game,
    sessionId = sessionId
) {
    private val mCurrentPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val currentPlayerId: StateFlow<Long?> = mCurrentPlayerId.asStateFlow()

    init {
        game.players.combine(game.order) { players, order -> players to order }
            .onEach { (players, order) ->
                mCurrentPlayerId.update {
                    order?.let { players[it].id }
                }
            }
            .launchIn(viewModelScope)
    }
}
