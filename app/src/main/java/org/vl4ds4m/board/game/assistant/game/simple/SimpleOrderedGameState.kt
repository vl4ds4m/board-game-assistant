package org.vl4ds4m.board.game.assistant.game.simple

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState

@Serializable
data class SimpleOrderedGameState(
    override val orderedPlayerIds: List<Long>
) : OrderedGameState
