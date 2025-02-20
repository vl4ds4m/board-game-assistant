package org.vl4ds4m.board.game.assistant.game.monopoly

import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyProperty
import org.vl4ds4m.board.game.assistant.game.state.PlayerState

@Suppress("unused")
class MonopolyPlayerState(
    override var score: Int = 0,
    var position: Int = 1,
    var entities: Set<MonopolyEntity> = setOf(),
    var propertyLevel: Map<MonopolyProperty, Int> = mapOf(),
    var inPrison: Boolean = false,
    var mercyCount: Int = 0
) : PlayerState
