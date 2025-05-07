package org.vl4ds4m.board.game.assistant.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.vl4ds4m.board.game.assistant.closeAndLog
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Users
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.title
import org.vl4ds4m.board.game.assistant.updateList
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket

class GameEmitter(
    gameEnv: GameEnv,
    private val scope: CoroutineScope,
    private val sessionEmitter: SessionEmitter
) {
    private val mRemotePlayers = MutableStateFlow<List<User>>(listOf())
    val remotePlayers: StateFlow<List<User>> = mRemotePlayers.asStateFlow()

    private var serverSocket: ServerSocket? = null
    private val playerSockets = MutableStateFlow<List<Socket>?>(null)

    private val emitterState = MutableStateFlow(NetworkGameState.REGISTRATION)

    private val sessionState = MutableStateFlow<GameSession?>(null)
    private val users: StateFlow<Users> = gameEnv.users

    private val produceGameState: () -> GameSession = gameEnv::save

    private val lastUpdate = MutableStateFlow(false)

    init {
        gameEnv.initialized.combine(gameEnv.completed) {
            init, comp -> init to comp
        }.onEach { (initialized, completed) ->
            val state = when {
                !initialized -> NetworkGameState.REGISTRATION
                completed -> NetworkGameState.END_GAME
                else -> NetworkGameState.IN_GAME
            }
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

    fun startEmit(id: String, type: GameType, name: String) {
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
        sessionEmitter.register(id, type, name, serverSocket.localPort)
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
        val networkPlayer = input.readObject()
            .let { it as String }
            .let { Json.decodeFromString<User>(it) }
            .also {
                mRemotePlayers.updateList { add(it) }
            }
        try {
            while (true) {
                val state = emitterState.value
                if (state == NetworkGameState.END_GAME && lastUpdate.value) {
                    sessionState.value = produceGameState().also {
                        emitInGameState(it, networkPlayer, output, input)
                    }
                    lastUpdate.value = false
                }
                if (state == NetworkGameState.IN_GAME) {
                    sessionState.value?.let {
                        emitInGameState(it, networkPlayer, output, input)
                    }
                } else {
                    emitState(
                        state, output, input,
                        users.value.isBound(networkPlayer),
                    )
                }
                delay(1000)
            }
        } finally {
            mRemotePlayers.updateList {
                removeIf { it.netDevId == networkPlayer.netDevId }
            }
        }
    }

    private fun emitState(
        state: NetworkGameState,
        output: ObjectOutputStream,
        input: ObjectInputStream,
        bound: Boolean
    ) {
        val actualState =
            if (bound) state
            else NetworkGameState.REGISTRATION
        output.writeObject(actualState.title)
        input.readObject()
    }

    private fun emitInGameState(
        session: GameSession,
        networkPlayer: User,
        output: ObjectOutputStream,
        input: ObjectInputStream
    ) {
        val bound = session.users.isBound(networkPlayer)
        emitState(NetworkGameState.IN_GAME, output, input, bound)
        if (bound) {
            Json.encodeToString(session)
                .let { output.writeObject(it) }
                .also { input.readObject() }
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

private fun Users.isBound(networkPlayer: User): Boolean = values.any {
    it.netDevId == networkPlayer.netDevId
}

private const val TAG = "GameEmitter"
