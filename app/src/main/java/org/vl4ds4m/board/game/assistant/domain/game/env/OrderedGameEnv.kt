package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.domain.Player

interface OrderedGameEnv : GameEnv {
    val orderedPlayerIds: StateFlow<List<Long>>

    val currentPlayerId: StateFlow<Long?>

    val currentPlayer: Player?
        get() = currentPlayerId.value?.let { players.value[it] }

    fun selectNextPlayerId()

    fun changeCurrentPlayerId(id: Long)

    fun changePlayerOrder(id: Long, order: Int)
}
