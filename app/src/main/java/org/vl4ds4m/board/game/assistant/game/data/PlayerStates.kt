package org.vl4ds4m.board.game.assistant.game.data

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyEntity
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyProperty

@Serializable
sealed interface PlayerState {
    val score: Int
}

@Serializable
data class Score(override val score: Int = 0) : PlayerState

@Suppress("unused")
// not yet @Serializable
class MonopolyPlayerState(
    override var score: Int = 0,
    var position: Int = 1,
    var entities: Set<MonopolyEntity> = setOf(),
    var propertyLevel: Map<MonopolyProperty, Int> = mapOf(),
    var inPrison: Boolean = false,
    var mercyCount: Int = 0
) : PlayerState
