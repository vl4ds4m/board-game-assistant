package org.vl4ds4m.board.game.assistant.game

import kotlinx.coroutines.flow.StateFlow

interface OrderedGame : Game {
    val nextPid: StateFlow<PID?>

    fun changeCurrentPid()

    fun changePlayerOrder(id: PID, order: Int)
}
