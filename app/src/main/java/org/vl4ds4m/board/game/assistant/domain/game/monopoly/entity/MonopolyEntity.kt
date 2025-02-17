package org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity

import org.vl4ds4m.board.game.assistant.domain.game.monopoly.MonopolyField

sealed interface MonopolyEntity : MonopolyField {
    val cost: Int get() = 1_000_000
    val rent: Int get() = 200_000
}
