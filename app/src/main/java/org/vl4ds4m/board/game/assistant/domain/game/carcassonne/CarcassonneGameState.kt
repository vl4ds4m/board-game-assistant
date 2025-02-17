package org.vl4ds4m.board.game.assistant.domain.game.carcassonne

import org.vl4ds4m.board.game.assistant.domain.game.state.OrderedGameState

class CarcassonneGameState(
    currentPlayerId: Long? = null,
    var onFinal: Boolean = false
) : OrderedGameState(currentPlayerId)
