package org.vl4ds4m.board.game.assistant.game.carcassonne

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState

@Serializable
data class CarcassonneGameState(
    override val orderedPlayerIds: List<Long>,
    val finalStage: Boolean
) : OrderedGameState {
    constructor(
        state: OrderedGameState,
        finalStage: Boolean
    ) : this(state.orderedPlayerIds, finalStage)
}
