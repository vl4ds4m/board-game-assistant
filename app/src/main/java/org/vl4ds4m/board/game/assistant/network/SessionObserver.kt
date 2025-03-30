package org.vl4ds4m.board.game.assistant.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.vl4ds4m.board.game.assistant.updateList
import java.net.DatagramPacket
import java.net.DatagramSocket

class SessionObserver(private val scope: CoroutineScope) {
    private var socket: DatagramSocket? = null

    private val mSessions: MutableStateFlow<List<RemoteSessionInfo>> =
        MutableStateFlow(listOf())
    val sessions: StateFlow<List<RemoteSessionInfo>> = mSessions.asStateFlow()

    init {
        scope.launch {
            while (true) {
                delay(7_000)
                mSessions.updateList {
                    val iterator = listIterator()
                    while (iterator.hasNext()) {
                        val session = iterator.next()
                        if (session.stale) iterator.remove()
                        else session.stale = true
                    }
                }
            }
        }
    }

    fun startObserve() {
        socket?.let {
            Log.e(TAG, "During start observe socket is still not null")
            it.close()
        }
        val socket = DatagramSocket(DISCOVER_REMOTE_GAMES_PORT).also {
            socket = it
        }
        scope.launch(Dispatchers.IO) {
            try {
                discoverSessions(socket)
            } catch (e: Exception) {
                Log.i(TAG, e.toString())
            }
        }
    }

    private fun discoverSessions(socket: DatagramSocket) {
        val size = 1000
        val buffer = ByteArray(size)
        while (true) {
            buffer.fill(0)
            val packet = DatagramPacket(buffer, size)
            socket.receive(packet)
            val session = String(buffer, 0, packet.length)
                .let { Json.decodeFromString<RemoteSessionInfo>(it) }
            mSessions.updateList {
                indexOfFirst { it.ip == session.ip }
                    .takeIf { it != -1 }
                    ?.also { set(it, session) }
                    ?: add(session)
            }
        }
    }

    fun stopObserve() {
        socket?.let {
            it.close()
            Log.i(TAG, "Socket is closed")
            socket = null
        }
    }
}

private const val TAG = "SessionObserver"
