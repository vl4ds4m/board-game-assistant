package org.vl4ds4m.board.game.assistant.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.vl4ds4m.board.game.assistant.closeAndLog
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.title
import org.vl4ds4m.board.game.assistant.updateList
import java.io.Closeable
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket

class GameEmitter(
    private val gameEnv: GameEnv,
    private val scope: CoroutineScope,
    private val sessionEmitter: SessionEmitter
) {
    private val mUsers = MutableStateFlow<List<User>>(listOf())
    val users: StateFlow<List<User>> = mUsers.asStateFlow()

    private var server: Server? = null
    private val connections = MutableStateFlow<List<Connection>?>(null)

    private val networkGameState = MutableStateFlow(NetworkGameState.REGISTRATION)
    private val gameSessionState = MutableStateFlow<GameSession?>(null)
    private val gameState: StateFlow<Pair<NetworkGameState, GameSession?>> =
        networkGameState.combine(gameSessionState) { ns, gs ->
            ns to gs
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = networkGameState.value to gameSessionState.value
        )

    private val produceGameState: () -> GameSession = gameEnv::save

    fun start(id: String, type: GameType, name: String) {
        closeSockets()
        val server = Server().also { server = it }
        connections.value = listOf()
        sessionEmitter.enable(id, type, name, server.port)
        launchGameStateUpdate()
        scope.launch(Dispatchers.IO) {
            server.use { _ ->
                while (true) {
                    val connection = server.accept() ?: break
                    if (!addAtomically(connection)) break
                    connection.startExchange()
                }
            }
        }
    }

    private fun launchGameStateUpdate() {
        gameEnv.initialized.combine(gameEnv.completed) { initialized, completed ->
            val state = when {
                !initialized -> NetworkGameState.REGISTRATION
                completed -> NetworkGameState.END_GAME
                else -> NetworkGameState.IN_GAME
            }
            if (state == NetworkGameState.END_GAME) {
                gameSessionState.value = produceGameState()
            }
            networkGameState.value = state
        }.launchIn(scope)
        scope.launch {
            while (true) {
                if (networkGameState.value == NetworkGameState.IN_GAME) {
                    gameSessionState.value = produceGameState()
                }
                delay(1000)
            }
        }
    }

    private fun addAtomically(connection: Connection): Boolean {
        val result = connections.value?.let {
            connections.compareAndSet(it, it + connection)
        } ?: false
        if (!result) {
            connection.close()
        }
        return result
    }

    fun stop() {
        sessionEmitter.disable()
        closeSockets()
    }

    private fun closeSockets() {
        server?.let {
            it.close()
            server = null
        }
        connections.getAndUpdate { null }
            ?.forEach { it.close() }
    }

    private inner class Server : Closeable {
        private val serverSocket = ServerSocket(0)

        val port: Int = serverSocket.localPort

        init {
            Log.i(EMITTER, "Open $SERVER($port)")
        }

        override fun close() {
            serverSocket.closeAndLog(EMITTER, SERVER)
        }

        fun accept(): Connection? {
            val socket: Socket
            try {
                socket = serverSocket.accept()
            } catch (e: Exception) {
                Log.i(EMITTER, "$SERVER: $e")
                return null
            }
            return Connection(socket)
        }
    }

    private inner class Connection(private val socket: Socket) : Closeable {
        private val address = socket.inetAddress

        private lateinit var input: ObjectInputStream
        private lateinit var output: ObjectOutputStream

        init {
            Log.i(EMITTER, "Open $CONNECTION($address)")
        }

        override fun close() {
            socket.closeAndLog(EMITTER, "$CONNECTION($address)")
        }

        fun startExchange() {
            scope.launch(Dispatchers.IO) {
                socket.use { _ ->
                    try {
                        input = ObjectInputStream(socket.getInputStream())
                        output = ObjectOutputStream(socket.getOutputStream())
                        run()
                    } catch (e: Exception) {
                        Log.i(EMITTER, "$CONNECTION($address): $e")
                    }
                }
            }
        }

        private suspend fun run(): Unit = withContext(Dispatchers.IO) {
            val user = input.readObject()
                .let { it as String }
                .let { Json.decodeFromString<User>(it) }
                .also {
                    mUsers.updateList { add(it) }
                }
            try {
                gameState.collectLatest { (networkState, sessionState) ->
                    when(networkState) {
                        NetworkGameState.REGISTRATION, NetworkGameState.EXIT -> {
                            emitState(networkState)
                        }
                        NetworkGameState.END_GAME -> {
                            if (user.isBound) {
                                emitGameSession(sessionState)
                                emitState(networkState)
                            } else {
                                emitState(NetworkGameState.REGISTRATION)
                            }
                        }
                        NetworkGameState.IN_GAME -> {
                            if (user.isBound) {
                                emitGameSession(sessionState)
                            } else {
                                emitState(NetworkGameState.REGISTRATION)
                            }
                        }
                    }
                }
            } finally {
                mUsers.updateList {
                    removeIf { it.netDevId == user.netDevId }
                }
            }
        }

        private fun emitState(state: NetworkGameState) {
            output.writeObject(state.title)
            input.readObject()
        }

        private fun emitGameSession(state: GameSession?) {
            state ?: return
            emitState(NetworkGameState.IN_GAME)
            Json.encodeToString(state)
                .let { output.writeObject(it) }
                .also { input.readObject() }
        }

        private val User.isBound: Boolean
            get() = gameSessionState.value?.users?.values?.any {
                it.netDevId == this.netDevId
            } ?: false
    }
}

private const val EMITTER = "GameEmitter"
private const val SERVER = "Server"
private const val CONNECTION = "Connection"
