package org.vl4ds4m.board.game.assistant.domain.game.state

open class OrderedGameState(
    var orderedPlayerIds: List<Long> = listOf()
) : GameState
