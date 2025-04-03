package org.vl4ds4m.board.game.assistant.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.vl4ds4m.board.game.assistant.closeAndLog
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.title
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket

class GameEmitter(
    gameEnv: GameEnv,
    private val scope: CoroutineScope,
    private val sessionEmitter: SessionEmitter
) {
    private var serverSocket: ServerSocket? = null
    private val playerSockets = MutableStateFlow<List<Socket>?>(null)

    private val networkPlayer = MutableStateFlow<NetworkPlayer?>(null)

    private val emitterState = MutableStateFlow(NetworkGameState.REGISTRATION)

    private val sessionState = MutableStateFlow<GameSession?>(null)

    private val produceGameState: () -> GameSession = gameEnv::save

    private val lastUpdate = MutableStateFlow(false)

    init {
        gameEnv.initialized.combine(gameEnv.completed) {
            init, comp -> init to comp
        }.combine(gameEnv.players) {
            lifecycle, players -> lifecycle to players
        }.combine(networkPlayer) {
            env, player -> env to player
        }.onEach { (env, networkPlayer) ->
            val (lifecycle, players) = env
            val (initialized, completed) = lifecycle
            val state = getState(
                initialized = initialized,
                completed = completed,
                players = players,
                networkPlayer = networkPlayer
            )
            emitterState.value = state
            lastUpdate.value = state == NetworkGameState.END_GAME
        }.launchIn(scope)
        scope.launch {
            while (true) {
                if (emitterState.value == NetworkGameState.IN_GAME) {
                    sessionState.value = produceGameState()
                }
                delay(1000)
            }
        }
    }

    fun startEmit(id: String, name: String) {
        serverSocket?.let {
            Log.e(TAG, "During start emit ServerSocket is still not null")
            closeServerSocket()
        }
        playerSockets.value?.let {
            Log.e(TAG, "During start emit PlayersSockets already initialized")
            closePlayerSockets()
        }
        val serverSocket = ServerSocket(0).also {
            Log.i(TAG, "Open ServerSocket(${it.localPort})")
            serverSocket = it
        }
        playerSockets.value = listOf()
        sessionEmitter.register(id, name, serverSocket.localPort)
        scope.launch(Dispatchers.IO) {
            while (true) {
                val socket: Socket
                try {
                    socket = serverSocket.accept()
                } catch (e: Exception) {
                    Log.i(TAG, "ServerSocket: $e")
                    break
                }
                Log.i(TAG, "Open PlayerSocket(${socket.inetAddress})")
                if (!addPlayerSocket(socket)) break
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

    private fun addPlayerSocket(socket: Socket): Boolean {
        val result = playerSockets.value?.let {
            playerSockets.compareAndSet(it, it + socket)
        } ?: false
        if (!result) {
            socket.closeAndLog(TAG, "PlayerSocket(${socket.inetAddress})")
        }
        return result
    }

    private suspend fun emit(
        input: ObjectInputStream,
        output: ObjectOutputStream
    ): Unit = withContext(Dispatchers.IO) {
        networkPlayer.value = input.readObject()
            .let { it as String }
            .let { Json.decodeFromString<NetworkPlayer>(it) }
        while (true) {
            val state = emitterState.value
            if (state == NetworkGameState.END_GAME && lastUpdate.value) {
                emitState(NetworkGameState.IN_GAME, output, input)
                produceGameState().also {
                    it.emitGameSession(output, input)
                    sessionState.value = it
                }
                lastUpdate.value = false
            }
            emitState(state, output, input)
            if (state == NetworkGameState.IN_GAME) {
                sessionState.value?.emitGameSession(output, input)
            }
            delay(2000)
        }
    }

    fun stopEmit() {
        sessionEmitter.unregister()
        closeServerSocket()
        closePlayerSockets()
    }

    private fun closeServerSocket() {
        serverSocket?.let {
            it.closeAndLog(TAG, "ServetSocket")
            serverSocket = null
        }
    }

    private fun closePlayerSockets() {
        playerSockets.getAndUpdate {
            null
        }?.forEach {
            it.closeAndLog(TAG, "PlayerSocket(${it.inetAddress})")
        }
    }
}

private const val TAG = "GameEmitter"

private fun getState(
    initialized: Boolean,
    completed: Boolean,
    players: Players,
    networkPlayer: NetworkPlayer?
): NetworkGameState = when {
    !initialized -> NetworkGameState.REGISTRATION
    completed -> NetworkGameState.END_GAME
    else -> {
        val bound = networkPlayer != null &&
            players.values.any {
                it.netDevId == networkPlayer.netDevId
            }
        if (bound) NetworkGameState.IN_GAME
        else NetworkGameState.REGISTRATION
    }
}

private fun emitState(
    state: NetworkGameState,
    output: ObjectOutputStream,
    input: ObjectInputStream
) {
    output.writeObject(state.title)
    input.readObject()
}

private fun GameSession.emitGameSession(output: ObjectOutputStream, input: ObjectInputStream) {
    Json.encodeToString(this)
        .let { output.writeObject(it) }
        .also { input.readObject() }
}
