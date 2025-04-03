package org.vl4ds4m.board.game.assistant.game

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

@Serializable
data class Player(
    val netDevId: String?,
    val name: String,
    val active: Boolean,
    val state: PlayerState
)
