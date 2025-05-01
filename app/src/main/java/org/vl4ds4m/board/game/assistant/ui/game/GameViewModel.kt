package org.vl4ds4m.board.game.assistant.ui.game

import android.net.nsd.NsdManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.network.GameEmitter
import org.vl4ds4m.board.game.assistant.network.NetworkPlayer
import org.vl4ds4m.board.game.assistant.network.SessionEmitter
import java.util.UUID

abstract class GameViewModel(
    private val gameEnv: GameEnv,
    sessionId: String?,
    app: BoardGameAssistantApp
) : ViewModel(), Game {
    private val sessionRepository: GameSessionRepository = app.sessionRepository

    private val gameEmitter: GameEmitter = SessionEmitter(
        app.applicationContext.getSystemService(NsdManager::class.java)
    ).let {
        GameEmitter(gameEnv, viewModelScope, it)
    }

    val userPlayer: StateFlow<NetworkPlayer?> = app.userDataRepository.run {
        userName.combine(netDevId) { name, id ->
            NetworkPlayer(name = name, netDevId = id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    }

    val remotePlayers: StateFlow<List<NetworkPlayer>> = gameEmitter.remotePlayers

    private val sessionId: String = if (sessionId == null) {
        UUID.randomUUID().toString()
    } else {
        gameEnv.initialize()
        sessionId
    }

    init {
        gameEnv.initializables.forEach { it.init(viewModelScope) }
        viewModelScope.launch {
            sessionId?.let { id ->
                sessionRepository.loadSession(id)
                    ?.let { gameEnv.load(it) }
                    ?: Log.e(TAG, "Can't load game session[id = $id] as it doesn't exist")
            }
            gameEmitter.startEmit(this@GameViewModel.sessionId, gameEnv.name.value)
        }
    }

    fun createGameUi(): GameUI = type.gameUiFactory.create(this)

    override fun onCleared() {
        if (initialized.value) {
            Log.d(TAG, "Complete game process")
            gameEnv.initializables.forEach { it.close() }
            gameEnv.save()
                .let { sessionRepository.saveSession(it, sessionId) }
        }
        gameEmitter.stopEmit()
        super.onCleared()
    }

    companion object {
        fun createFactory(
            sessionId: String?,
            producer: GameViewModelProducer<GameViewModel>
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer<GameViewModel> {
                val app = BoardGameAssistantApp.from(this)
                if (sessionId == null) {
                    val game = app.gameEnvRepository.extract()
                    producer.createViewModel(game, app)
                } else {
                    producer.createViewModel(sessionId, app)
                }
            }
        }
    }
}

private const val TAG = "GameViewModel"
