package org.vl4ds4m.board.game.assistant.game.monopoly

import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyProperty

class MonopolyPlayerState(
    var position: Int = 1,
    var entities: Set<MonopolyEntity> = setOf(),
    var propertyLevel: Map<MonopolyProperty, Int> = mapOf(),
    var inPrison: Boolean = false,
    var mercyCount: Int = 0
)
