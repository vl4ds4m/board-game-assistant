package org.vl4ds4m.board.game.assistant.domain.game.monopoly

import org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.domain.game.state.OrderedGameState

class MonopolyGameState(
    currentPlayerId: Long? = null,
    var entityOwner: Map<MonopolyEntity, Long> = mapOf(),
    var playerState: Map<Long, MonopolyPlayerState> = mapOf(),
    var repeatCount: Int = 0,
    var afterStepField: MonopolyField? = null
) : OrderedGameState(currentPlayerId)
