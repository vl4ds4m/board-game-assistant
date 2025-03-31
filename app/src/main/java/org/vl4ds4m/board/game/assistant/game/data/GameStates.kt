package org.vl4ds4m.board.game.assistant.game.data

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyField
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyEntity

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

// not yet @Serializable
data class MonopolyGameState(
    override val orderedPlayerIds: List<Long>,
    var entityOwner: Map<MonopolyEntity, Long> = mapOf(),
    var repeatCount: Int = 0,
    var afterStepField: MonopolyField? = null
) : OrderedGameState {
    constructor(
        state: OrderedGameState,
        entityOwner: Map<MonopolyEntity, Long> = mapOf(),
        repeatCount: Int = 0,
        afterStepField: MonopolyField? = null
    ) : this(
        state.orderedPlayerIds,
        entityOwner,
        repeatCount,
        afterStepField
    )
}
