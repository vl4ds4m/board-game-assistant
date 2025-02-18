package org.vl4ds4m.board.game.assistant.domain.game.state

open class OrderedGameState(
    var orderedPlayerIds: List<Long> = listOf(),
    var currentPlayerId: Long? = null
) : GameState
