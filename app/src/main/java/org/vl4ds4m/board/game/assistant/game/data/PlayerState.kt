package org.vl4ds4m.board.game.assistant.game.data

import kotlinx.serialization.Serializable

typealias PlayerStateData = Map<String, String>

@Serializable
data class PlayerState(
    val score: Int,
    val data: PlayerStateData
)
/*
@Serializable
data class Score(override val score: Int = 0) : PlayerState

@Serializable
data class MonopolyPlayerState(
    override val score: Int = 0,
    val position: Int = 1,
    val inPrison: Boolean = false,
) : PlayerState*/
