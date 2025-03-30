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
import java.net.SocketException

class GameObserver(private val scope: CoroutineScope) {
    private var socket: Socket? = null

    private val mObserverState: MutableStateFlow<ObserverState> =
        MutableStateFlow(ObserverState.REGISTRATION)
    val observerState: StateFlow<ObserverState> = mObserverState.asStateFlow()

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
                socket.connect(address)
                val output = ObjectOutputStream(socket.getOutputStream())
                val input = ObjectInputStream(socket.getInputStream())
                observe(output, input)
            } catch (e: SocketException) {
                Log.i(TAG, "Socket is closed")
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                mObserverState.value = ObserverState.EXIT
            }
        }
    }

    private fun observe(output: ObjectOutputStream, input: ObjectInputStream) {
        NetworkPlayer(FAKE_NAME, FAKE_MAC)
            .let { Json.encodeToString(it) }
            .let { output.writeObject(it) }
        while (true) {
            val state = when (val header = input.readObject() as String) {
                ObserverState.REGISTRATION.title -> ObserverState.REGISTRATION
                ObserverState.IN_GAME.title -> ObserverState.IN_GAME
                ObserverState.END_GAME.title -> ObserverState.END_GAME
                ObserverState.EXIT.title -> ObserverState.EXIT
                else -> throw NetworkException("Invalid header: ${header.short}")
            }
            if (state == ObserverState.IN_GAME) {
                mSessionState.value = input.readObject()
                    .let { it as String }
                    .let { Json.decodeFromString<NetworkSession>(it) }
                    .session
            }
            mObserverState.value = state
        }
    }

    fun stopObserve() {
        socket?.let {
            it.close()
            socket = null
        }
    }
}

private const val TAG = "GameObserver"

private const val FAKE_NAME = "Abc"
private const val FAKE_MAC = "01:02:03:04:05:06"
