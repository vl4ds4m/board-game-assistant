package org.vl4ds4m.board.game.assistant.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.network.GameEmitter
import java.util.UUID

abstract class GameViewModel(
    private val gameEnv: GameEnv,
    sessionId: String?,
    app: BoardGameAssistantApp
) : ViewModel(), Game {
    private val sessionRepository: GameSessionRepository = app.sessionRepository

    private val gameEmitter = GameEmitter(gameEnv, viewModelScope, app.sessionEmitter)

    val remotePlayers: StateFlow<List<User>> = gameEmitter.users
        .combine(app.userDataRepository.user) { users, user ->
            users.map { it.copy(self = false) } + user
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

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
                    ?.let {
                        val user = app.userDataRepository.user.first()
                        it.changeCurrentUser(user.netDevId)
                    }
                    ?.let { gameEnv.load(it) }
                    ?: Log.e(TAG, "Can't load game session[id = $id] as it doesn't exist")
            }
            gameEmitter.start(
                this@GameViewModel.sessionId,
                gameEnv.type,
                gameEnv.name.value
            )
        }
    }

    val gameUi: GameUI = gameEnv.type.uiFactory.create(gameEnv)

    fun stopGameProcess() {
        gameEnv.stop()
        saveCurrentState(initialized.value)
    }

    private fun saveCurrentState(initialized: Boolean) {
        if (initialized) {
            val state = gameEnv.save()
            sessionRepository.saveSession(state, sessionId)
        }
    }

    override fun onCleared() {
        val initialized = initialized.value
        if (initialized) {
            Log.d(TAG, "Complete game process")
            gameEnv.initializables.forEach { it.close() }
        }
        saveCurrentState(initialized)
        gameEmitter.stop()
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
