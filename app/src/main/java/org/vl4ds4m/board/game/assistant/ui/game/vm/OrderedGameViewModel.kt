package org.vl4ds4m.board.game.assistant.ui.game.vm

import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

abstract class OrderedGameViewModel(
    game: OrderedGameEnv,
    sessionId: Long? = null
) : GameViewModel(
    game = game,
    sessionId = sessionId
) {
    val currentPlayerId: StateFlow<Long?> = game.currentPlayerId
}
