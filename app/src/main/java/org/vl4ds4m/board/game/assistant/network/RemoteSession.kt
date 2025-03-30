package org.vl4ds4m.board.game.assistant.network

data class RemoteSession(
    val id: Long,
    val name: String,
    val ip: String,
    val port: Int
)
