package org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity

sealed interface Supplier : MonopolyEntity

data object PowerStation : Supplier
data object WaterStation : Supplier
