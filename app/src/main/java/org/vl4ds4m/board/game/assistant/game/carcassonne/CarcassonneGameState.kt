package org.vl4ds4m.board.game.assistant.game.carcassonne

import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState

class CarcassonneGameState(
    state: OrderedGameState,
    val finalStage: Boolean
) : OrderedGameState(state.orderedPlayerIds)
