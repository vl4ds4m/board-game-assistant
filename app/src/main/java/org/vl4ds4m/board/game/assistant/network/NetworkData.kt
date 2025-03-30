package org.vl4ds4m.board.game.assistant.network

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.data.GameSession

@Serializable
data class NetworkPlayer(val name: String, val mac: String)

@Serializable
data class NetworkSession(val session: GameSession)

@Serializable
data class RemoteSessionInfo(
    val id: Long,
    val name: String,
    val ip: String,
    val port: Int,
) {
    var stale: Boolean = false
}

const val DISCOVER_REMOTE_GAMES_PORT = 14931