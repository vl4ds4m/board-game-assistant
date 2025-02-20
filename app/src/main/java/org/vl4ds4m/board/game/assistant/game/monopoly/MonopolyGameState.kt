package org.vl4ds4m.board.game.assistant.game.monopoly

import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.game.state.OrderedGameState

class MonopolyGameState(
    orderedPlayerIds: List<Long> = listOf(),
    var entityOwner: Map<MonopolyEntity, Long> = mapOf(),
    var playerState: Map<Long, MonopolyPlayerState> = mapOf(),
    var repeatCount: Int = 0,
    var afterStepField: MonopolyField? = null
) : OrderedGameState(
    orderedPlayerIds = orderedPlayerIds
)
