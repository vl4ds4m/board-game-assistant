package org.vl4ds4m.board.game.assistant.game.monopoly

import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState

class MonopolyGameState(
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
