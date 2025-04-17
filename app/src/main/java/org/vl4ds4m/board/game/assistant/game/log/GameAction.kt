package org.vl4ds4m.board.game.assistant.game.log

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

typealias GameActionData = Map<String, String>

@Serializable
data class GameAction(val type: String, val data: GameActionData) {
    fun toJson(): String = Json.encodeToString(this)

    companion object {
        const val CHANGE_CURRENT_PLAYER  = "change_current_player"
        const val CHANGE_PLAYER_SCORE  = "change_player_score"

        fun fromJson(value: String): GameAction = Json.decodeFromString(value)
    }
}

/*@Serializable
data class CurrentPlayerChangeAction(
    val oldPlayerId: Long?,
    val newPlayerId: Long?
) : GameAction

@Serializable
data class PlayerStateChangeAction(
    val playerId: Long,
    val oldState: PlayerState,
    val newState: PlayerState
) : GameAction*/
