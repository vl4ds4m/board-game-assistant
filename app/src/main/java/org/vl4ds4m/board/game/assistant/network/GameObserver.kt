package org.vl4ds4m.board.game.assistant.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.vl4ds4m.board.game.assistant.closeAndLog
import org.vl4ds4m.board.game.assistant.data.repository.UserDataRepository
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.short
import org.vl4ds4m.board.game.assistant.title
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class GameObserver(
    private val userDataRepository: UserDataRepository,
    private val scope: CoroutineScope
) {
    private var socket: Socket? = null

    private val mObserverState: MutableStateFlow<NetworkGameState> =
        MutableStateFlow(NetworkGameState.REGISTRATION)
    val observerState: StateFlow<NetworkGameState> = mObserverState.asStateFlow()

    private val mSessionState: MutableStateFlow<GameSession?> =
        MutableStateFlow(null)
    val sessionState: StateFlow<GameSession?> = mSessionState.asStateFlow()

    fun startObserve(sessionInfo: RemoteSessionInfo) {
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
                InetSocketAddress(sessionInfo.ip, sessionInfo.port).let {
                    socket.connect(it, 3000)
                }
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

    private suspend fun observe(
        output: ObjectOutputStream,
        input: ObjectInputStream
    ): Unit = withContext(Dispatchers.IO) {
        userDataRepository.run {
            userName.first() to netDevId.first()
        }.let { (name, id) ->
            NetworkPlayer(name, id)
        }.let {
            Json.encodeToString(it)
        }.let {
            output.writeObject(it)
        }
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
            it.closeAndLog(TAG, "Socket")
            socket = null
        }
    }
}

private const val TAG = "GameObserver"
