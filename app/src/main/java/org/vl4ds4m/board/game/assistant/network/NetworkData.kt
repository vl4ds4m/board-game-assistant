package org.vl4ds4m.board.game.assistant.network

import kotlinx.serialization.Serializable

@Serializable
data class RemoteSessionInfo(
    val id: String,
    val name: String,
    val ip: String,
    val port: Int,
) {
    companion object {
        const val TXT_ID = "rs_id"
        const val TXT_NAME = "rs_name"
    }
}
