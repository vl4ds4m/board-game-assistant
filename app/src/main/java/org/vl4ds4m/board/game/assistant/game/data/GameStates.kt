package org.vl4ds4m.board.game.assistant.game.data

import kotlinx.serialization.Serializable

@Serializable
sealed interface GameState

@Serializable
sealed interface OrderedGameState : GameState {
    val orderedPlayerIds: List<Long>
}

@Serializable
data class SimpleOrderedGameState(
    override val orderedPlayerIds: List<Long>
) : OrderedGameState

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
