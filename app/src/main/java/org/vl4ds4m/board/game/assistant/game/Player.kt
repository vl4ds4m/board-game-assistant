package org.vl4ds4m.board.game.assistant.game

import org.vl4ds4m.board.game.assistant.game.state.PlayerState

data class Player(
    val name: String,
    val active: Boolean,
    val state: PlayerState
)
