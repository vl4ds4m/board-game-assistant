package org.vl4ds4m.board.game.assistant.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.short
import org.vl4ds4m.board.game.assistant.title
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class GameObserver(private val scope: CoroutineScope) {
    private var socket: Socket? = null

    private val mObserverState: MutableStateFlow<NetworkGameState> =
        MutableStateFlow(NetworkGameState.REGISTRATION)
    val observerState: StateFlow<NetworkGameState> = mObserverState.asStateFlow()

    private val mSessionState: MutableStateFlow<GameSession?> =
        MutableStateFlow(null)
    val sessionState: StateFlow<GameSession?> = mSessionState.asStateFlow()

    fun startObserve(address: InetSocketAddress) {
        socket?.let {
            Log.e(TAG, "During start observe socket is still not null")
            it.close()
        }
        val socket = Socket().also {
            it.bind(null)
            socket = it
        }
        scope.launch(Dispatchers.IO) {
            try {
                socket.connect(address, 3000)
                Log.i(TAG, "Open Socket(local=${socket.localPort}, remote=${socket.port})")
                val output = ObjectOutputStream(socket.getOutputStream())
                val input = ObjectInputStream(socket.getInputStream())
                observe(output, input)
            } catch (e: Exception) {
                Log.i(TAG, "Socket: $e")
                mObserverState.value = NetworkGameState.EXIT
            }
        }
    }

    private fun observe(output: ObjectOutputStream, input: ObjectInputStream) {
        NetworkPlayer(FAKE_NAME, FAKE_MAC)
            .let { Json.encodeToString(it) }
            .let { output.writeObject(it) }
        while (true) {
            val state = when (val header = input.readObject() as String) {
                NetworkGameState.REGISTRATION.title -> NetworkGameState.REGISTRATION
                NetworkGameState.IN_GAME.title -> NetworkGameState.IN_GAME
                NetworkGameState.END_GAME.title -> NetworkGameState.END_GAME
                NetworkGameState.EXIT.title -> NetworkGameState.EXIT
                else -> throw NetworkException("Invalid header: ${header.short}")
            }
            output.writeObject(null)
            if (state == NetworkGameState.IN_GAME) {
                mSessionState.value = input.readObject()
                    .let { it as String }
                    .let { Json.decodeFromString<GameSession>(it) }
                output.writeObject(null)
            }
            mObserverState.value = state
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

private const val TAG = "GameObserver"

private const val FAKE_NAME = "Abc"
private const val FAKE_MAC = "01:02:03:04:05:06"
