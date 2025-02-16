package org.vl4ds4m.board.game.assistant.domain.game.monopoly

import org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.domain.game.state.OrderedGameState

class MonopolyGameState(
    order: Int? = null,
    var entityOwner: Map<MonopolyEntity, Long> = mapOf(),
    var playerState: Map<Long, MonopolyPlayerState> = mapOf()
) : OrderedGameState(order)
