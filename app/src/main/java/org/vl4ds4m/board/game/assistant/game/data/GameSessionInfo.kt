package org.vl4ds4m.board.game.assistant.game.data

import org.vl4ds4m.board.game.assistant.game.GameType

class GameSessionInfo(
    val id: String,
    val completed: Boolean,
    val type: GameType,
    val name: String,
    val startTime: Long?,
    val stopTime: Long?
)
