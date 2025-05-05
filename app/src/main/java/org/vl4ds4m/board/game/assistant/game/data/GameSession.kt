package org.vl4ds4m.board.game.assistant.game.data

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.OrderedPlayers

@Serializable
data class GameSession(
    val completed: Boolean,
    val type: GameType,
    val name: String,
    val players: OrderedPlayers,
    val currentPlayerId: Long?,
    val nextNewPlayerId: Long,
    val startTime: Long?,
    val stopTime: Long?,
    val duration: Long?,
    val timeout: Boolean,
    val secondsUntilEnd: Int,
    val actions: Actions,
    val currentActionPosition: Int
)
