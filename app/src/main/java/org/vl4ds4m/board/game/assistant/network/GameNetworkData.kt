package org.vl4ds4m.board.game.assistant.network

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.data.GameSession

@Serializable
data class NetworkPlayer(val name: String, val mac: String)

@Serializable
data class NetworkSession(val session: GameSession)
