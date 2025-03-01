package org.vl4ds4m.board.game.assistant.game.monopoly

import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState

class MonopolyGameState(
    state: OrderedGameState,
    var entityOwner: Map<MonopolyEntity, Long> = mapOf(),
    var repeatCount: Int = 0,
    var afterStepField: MonopolyField? = null
) : OrderedGameState(state.orderedPlayerIds)
