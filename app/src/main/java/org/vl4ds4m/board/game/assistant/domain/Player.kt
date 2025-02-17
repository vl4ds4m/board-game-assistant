package org.vl4ds4m.board.game.assistant.domain

data class Player(
    val name: String,
    val active: Boolean,
    val score: Int
)
