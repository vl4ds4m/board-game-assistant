package org.vl4ds4m.board.game.assistant.game.data

import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Players

data class GameSession(
    val completed: Boolean,
    val type: GameType,
    val name: String,
    val players: Players,
    val currentPlayerId: Long?,
    val nextNewPlayerId: Long,
    val startTime: Long?,
    val stopTime: Long?,
    val timeout: Boolean,
    val secondsUntilEnd: Int,
    val actions: Actions,
    val currentActionPosition: Int,
    val additional: GameState?
)
