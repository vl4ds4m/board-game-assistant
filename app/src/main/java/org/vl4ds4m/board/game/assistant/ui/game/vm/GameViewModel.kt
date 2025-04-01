package org.vl4ds4m.board.game.assistant.ui.game.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.network.GameEmitter

abstract class GameViewModel(
    private val gameEnv: GameEnv,
    private val sessionId: Long?,
    extras: CreationExtras
) : ViewModel(), Game by gameEnv {
    private val sessionRepository: GameSessionRepository =
        extras[APPLICATION_KEY]
            .let { it as BoardGameAssistantApp }
            .sessionRepository

    private val gameEmitter = GameEmitter(
        viewModelScope,
        gameEnv.initialized,
        gameEnv.completed,
        gameEnv::save
    )

    init {
        if (sessionId != null) initialize()
    }

    final override fun initialize() {
        Log.d(TAG, "Initiate game process")
        viewModelScope.launch {
            sessionId?.let { id ->
                sessionRepository.loadSession(id)
                    ?.let { gameEnv.load(it) }
                    ?: Log.e(TAG, "Can't load game session[id = $id] as it doesn't exist")
            }
            gameEnv.initializables.forEach { it.init(viewModelScope) }
        }
        gameEmitter.startEmit()
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
            sessionId: Long?,
            producer: GameViewModelProducer<GameViewModel>
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer<GameViewModel> {
                val app = get(APPLICATION_KEY)
                    .let { it as BoardGameAssistantApp }
                if (sessionId == null) {
                    val game = app.gameRepository.extract()
                    producer.createViewModel(game, this)
                } else {
                    producer.createViewModel(sessionId, this)
                }
            }
        }
    }
}

private const val TAG = "GameViewModel"
