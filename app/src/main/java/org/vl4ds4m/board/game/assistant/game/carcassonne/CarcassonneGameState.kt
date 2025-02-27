package org.vl4ds4m.board.game.assistant.game.carcassonne

import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState

class CarcassonneGameState(
    orderedPlayerIds: List<Long> = listOf(),
    var onFinal: Boolean = false
) : OrderedGameState(
    orderedPlayerIds = orderedPlayerIds
)
