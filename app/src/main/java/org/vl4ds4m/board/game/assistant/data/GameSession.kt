package org.vl4ds4m.board.game.assistant.data

import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.state.GameState
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.log.GameAction

class GameSession(
    var type: GameType? = null,
    var completed: Boolean = false,
    var name: String = "",
    var players: Map<Long, Player> = mapOf(),
    var currentPlayerId: Long? = null,
    var nextPlayerId: Long? = null,
    var startTime: Long? = null,
    var timeout: Boolean = false,
    var secondsToEnd: Int = 0,
    var actions: List<GameAction> = listOf(),
    var currentActionPosition: Int = 0,
    var state: GameState? = null,
    var fake: Boolean = false
)
