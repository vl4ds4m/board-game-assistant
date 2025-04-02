package org.vl4ds4m.board.game.assistant.network

import android.net.nsd.NsdServiceInfo
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPlayer(val name: String, val mac: String)

data class RemoteSessionInfo(
    val id: Long,
    val name: String,
    val ip: String,
    val port: Int,
) {
    companion object {
        const val TXT_ID = "remote_session_id"
        const val TXT_NAME = "remote_session_name"

        fun from(info: NsdServiceInfo): RemoteSessionInfo? {
            val attrs = info.attributes
            val id = attrs[TXT_ID]?.let {
                String(it).toLong()
            } ?: return null
            val name = attrs[TXT_NAME]?.let {
                String(it)
            } ?: return null
            return RemoteSessionInfo(
                id = id,
                name = name,
                ip = info.host.toString(),
                port = info.port
            )
        }
    }
}
