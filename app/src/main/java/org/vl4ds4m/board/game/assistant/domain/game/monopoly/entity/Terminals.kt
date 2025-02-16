package org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity

sealed interface Terminal : MonopolyEntity {
    companion object {
        val A: Terminal = TA
        val B: Terminal = TB
        val C: Terminal = TC
        val D: Terminal = TD
    }
}

private data object TA : Terminal
private data object TB : Terminal
private data object TC : Terminal
private data object TD : Terminal
