package org.vl4ds4m.board.game.assistant.game.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

typealias PlayerStateData = Map<String, String>

@Serializable
data class PlayerState(
    val score: Int,
    val data: PlayerStateData
) {
    fun toJson(): String = Json.encodeToString(this)

    companion object {
        val Empty = PlayerState(score = 0, data = mapOf())

        fun fromJson(value: String): PlayerState = Json.decodeFromString(value)
    }
}

fun PlayerStateData.copy(
    vararg updates: Pair<String, String>
): PlayerStateData = toMutableMap().apply { putAll(updates) }
