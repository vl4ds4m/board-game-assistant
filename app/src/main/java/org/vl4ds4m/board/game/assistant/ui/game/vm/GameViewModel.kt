package org.vl4ds4m.board.game.assistant.ui.game.vm

import android.net.nsd.NsdManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.network.GameEmitter
import org.vl4ds4m.board.game.assistant.network.SessionEmitter
import java.util.UUID

abstract class GameViewModel(
    private val gameEnv: GameEnv,
    sessionId: String?,
    app: BoardGameAssistantApp
) : ViewModel(), Game by gameEnv {
    private val sessionRepository: GameSessionRepository = app.sessionRepository

    private val gameEmitter: GameEmitter = SessionEmitter(
        app.applicationContext.getSystemService(NsdManager::class.java)
    ).let {
        GameEmitter(gameEnv, viewModelScope, it)
    }

    private val sessionId: String

    init {
        gameEnv.initializables.forEach { it.init(viewModelScope) }
        this.sessionId = if (sessionId == null) {
            UUID.randomUUID().toString()
        } else {
            initialize()
            sessionId
        }
        viewModelScope.launch {
            sessionId?.let { id ->
                sessionRepository.loadSession(id)
                    ?.let { gameEnv.load(it) }
                    ?: Log.e(TAG, "Can't load game session[id = $id] as it doesn't exist")
            }
            gameEmitter.startEmit(this@GameViewModel.sessionId, gameEnv.name.value)
        }
    }

    final override fun initialize() {
        Log.d(TAG, "Initiate game process")
        gameEnv.initialize()
    }

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
                    val game = app.gameRepository.extract()
                    producer.createViewModel(game, app)
                } else {
                    producer.createViewModel(sessionId, app)
                }
            }
        }
    }
}

private const val TAG = "GameViewModel"
