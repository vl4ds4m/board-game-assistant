package org.vl4ds4m.board.game.assistant.game.data

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.OrderedPlayers
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Users

@Serializable
data class GameSession(
    val completed: Boolean,
    val type: GameType,
    val name: String,
    val players: OrderedPlayers,
    val users: Users,
    val currentPid: PID?,
    val nextNewPid: PID,
    val startTime: Long?,
    val stopTime: Long?,
    val duration: Long?,
    val timeout: Boolean,
    val secondsUntilEnd: Int,
    val actions: Actions,
    val currentActionPosition: Int
)
