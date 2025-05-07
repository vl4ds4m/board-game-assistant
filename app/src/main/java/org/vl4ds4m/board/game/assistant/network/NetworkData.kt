package org.vl4ds4m.board.game.assistant.network

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.GameType

@Serializable
data class RemoteSessionInfo(
    val id: String,
    val type: GameType,
    val name: String,
    val ip: String,
    val port: Int,
) {
    companion object {
        const val TXT_ID = "rs_id"
        const val TXT_TYPE = "rs_type"
        const val TXT_NAME = "rs_name"
    }
}
