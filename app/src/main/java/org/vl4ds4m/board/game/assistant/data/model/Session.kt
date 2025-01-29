package org.vl4ds4m.board.game.assistant.data.model

import org.vl4ds4m.board.game.assistant.data.GameType
import org.vl4ds4m.board.game.assistant.data.Player
import java.time.Instant

class Session(
    val type: GameType,
    val name: String,
    val players: List<Player>,
    val startTime: Instant,
    val stopTime: Instant,
    val completed: Boolean
)
