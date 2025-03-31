package org.vl4ds4m.board.game.assistant.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.title
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket

class GameEmitter(
    private val scope: CoroutineScope,
    isGameInitialized: StateFlow<Boolean>,
    isGameCompleted: StateFlow<Boolean>,
    produceGameState: () -> GameSession
) {
    private var serverSocket: ServerSocket? = null
    private val playerSockets: MutableCollection<Socket> = mutableListOf()

    private val emitterState = MutableStateFlow(NetworkGameState.REGISTRATION)

    private val sessionState = MutableStateFlow<GameSession?>(null)

    private val lastUpdate = MutableStateFlow(false)

    init {
        scope.launch {
            isGameInitialized.combine(isGameCompleted) { initialized, completed ->
                val state =
                    if (!initialized) NetworkGameState.REGISTRATION
                    else if (completed) NetworkGameState.END_GAME
                    else NetworkGameState.IN_GAME
                emitterState.value = state
                lastUpdate.value = state == NetworkGameState.END_GAME
            }
        }
        scope.launch {
            while (true) {
                if (emitterState.value == NetworkGameState.IN_GAME) {
                    sessionState.value = produceGameState()
                }
                delay(1000)
            }
        }
    }

    fun startEmit() {
        serverSocket?.let {
            Log.e(TAG, "During start emit ServerSocket is still not null")
            closeServerSocket()
        }
        playerSockets.takeIf { it.isNotEmpty() }
            ?.let {
                Log.e(TAG, "During start emit there are PlayersSockets")
                closePlayerSockets()
            }
        val serverSocket = ServerSocket(0).also {
            serverSocket = it
        }
        scope.launch(Dispatchers.IO) {
            while (true) {
                val socket: Socket
                try {
                    socket = serverSocket.accept()
                } catch (e: Exception) {
                    Log.i(TAG, "ServerSocket: $e")
                    break
                }
                playerSockets.add(socket)
                scope.launch(Dispatchers.IO) {
                    try {
                        val input = ObjectInputStream(socket.getInputStream())
                        val output = ObjectOutputStream(socket.getOutputStream())
                        emit(input, output)
                    } catch (e: Exception) {
                        Log.i(TAG, "PlayerSocket(${socket.inetAddress}): $e")
                    }
                }
            }
        }
    }

    private suspend fun emit(
        input: ObjectInputStream,
        output: ObjectOutputStream
    ): Unit = withContext(Dispatchers.IO) {
        input.readObject()
            .let { it as String }
            .let { Json.decodeFromString<NetworkPlayer>(it) }
        while (true) {
            val state = emitterState.value
            if (state == NetworkGameState.END_GAME && lastUpdate.value) {
                output.writeObject(NetworkGameState.IN_GAME)
                emitGameSession(output)
                lastUpdate.value = false
            }
            output.writeObject(state.title)
            if (state == NetworkGameState.IN_GAME) {
                emitGameSession(output)
            }
            delay(2000)
        }
    }

    private fun emitGameSession(output: ObjectOutputStream) {
        sessionState.value?.let { Json.encodeToString(it) }
            ?.let { output.writeObject(it) }
    }

    fun stopEmit() {
        closeServerSocket()
        closePlayerSockets()
    }

    private fun closeServerSocket() {
        serverSocket?.let {
            it.close()
            Log.i(TAG, "ServetSocket is closed")
            serverSocket = null
        }
    }

    private fun closePlayerSockets() {
        playerSockets.forEach {
            it.close()
            Log.i(TAG, "PlayerSocket(${it.inetAddress}) is closed")
        }
        playerSockets.clear()
    }
}

private const val TAG = "GameEmitter"
