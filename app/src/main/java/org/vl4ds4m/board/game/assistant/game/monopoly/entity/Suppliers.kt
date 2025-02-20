package org.vl4ds4m.board.game.assistant.game.monopoly.entity

sealed interface Supplier : MonopolyEntity {
    override val rent: Int get() = 100_000
}

data object PowerStation : Supplier
data object WaterStation : Supplier
