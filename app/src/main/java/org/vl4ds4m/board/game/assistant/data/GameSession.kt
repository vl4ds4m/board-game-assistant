package org.vl4ds4m.board.game.assistant.data

import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.PlayerState

class GameSession(
    var type: GameType? = null,
    var name: String? = null,
    var players: List<Player>? = null,
    var nextPlayerId: Long? = null,
    var order: Int? = null,
    var playerStates: Map<Long, PlayerState<*>>? = null,
    var fake: Boolean = false
)
