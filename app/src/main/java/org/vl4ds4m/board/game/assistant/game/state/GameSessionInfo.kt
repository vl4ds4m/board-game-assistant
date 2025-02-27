package org.vl4ds4m.board.game.assistant.game.state

import org.vl4ds4m.board.game.assistant.game.GameType

class GameSessionInfo(
    val id: Long,
    val completed: Boolean,
    val type: GameType,
    val name: String,
    val startTime: Long,
    val stopTime: Long
)
