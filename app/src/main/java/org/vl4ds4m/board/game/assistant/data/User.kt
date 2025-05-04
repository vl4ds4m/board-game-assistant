package org.vl4ds4m.board.game.assistant.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val netDevId: String,
    val self: Boolean,
    val name: String,
    // val image
) {
    companion object {
        val Empty = User(
            netDevId = "",
            self = false,
            name = ""
        )
    }
}
