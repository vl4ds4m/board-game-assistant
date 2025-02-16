package org.vl4ds4m.board.game.assistant.data

import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.state.GameState
import org.vl4ds4m.board.game.assistant.domain.player.Player

class GameSession(
    var type: GameType? = null,
    var completed: Boolean = false,
    var name: String = "",
    var players: List<Player> = listOf(),
    var nextPlayerId: Long? = null,
    var startTime: Long? = null,
    var timeout: Boolean = false,
    var secondsToEnd: Int = 0,
    var state: GameState? = null,
    var fake: Boolean = false
)
