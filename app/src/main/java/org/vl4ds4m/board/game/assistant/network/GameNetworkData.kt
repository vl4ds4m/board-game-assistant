package org.vl4ds4m.board.game.assistant.network

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.data.GameSession

sealed interface NetworkData

@Serializable
data object SessionRequest : NetworkData

@Serializable
data object EndGameResponse : NetworkData

@Serializable
data class NetworkPlayer(val name: String, val mac: String) : NetworkData

@Serializable
data class NetworkSession(val session: GameSession) : NetworkData
