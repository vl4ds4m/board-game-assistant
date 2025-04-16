package org.vl4ds4m.board.game.assistant.game.data

import kotlinx.serialization.Serializable

@Serializable
sealed interface GameState

@Serializable
data class OrderedGameState(val orderedPlayerIds: List<Long>) : GameState
