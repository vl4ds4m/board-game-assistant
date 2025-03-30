package org.vl4ds4m.board.game.assistant.game.data

interface GameState

interface OrderedGameState : GameState {
    val orderedPlayerIds: List<Long>
}
