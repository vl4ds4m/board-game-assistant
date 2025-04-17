package org.vl4ds4m.board.game.assistant.game.log

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

typealias GameActionData = Map<String, String>

@Serializable
data class GameAction(val type: String, val data: GameActionData) {
    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(value: String): GameAction = Json.decodeFromString(value)
    }
}
