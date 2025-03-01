package org.vl4ds4m.board.game.assistant.ui.game.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.env.GameEnv

abstract class GameViewModel(
    private val gameEnv: GameEnv,
    private val sessionId: Long?,
    private val sessionRepository: GameSessionRepository
) : ViewModel(), Game by gameEnv {
    init {
        Log.d(TAG, "Initiate ${this::class.simpleName}")
        viewModelScope.launch {
            sessionId?.let { id ->
                sessionRepository.loadSession(id)
                    ?.let { gameEnv.load(it) }
                    ?: Log.e(TAG, "Can't load game session[id = $id] as it doesn't exist")
            }
            gameEnv.initializables.forEach { it.init(viewModelScope) }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "Clear ${this::class.simpleName}")
        gameEnv.initializables.forEach { it.close() }
        gameEnv.save()
            .let { sessionRepository.saveSession(it, sessionId) }
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
                val sessionRepository = app.sessionRepository
                if (sessionId == null) {
                    val game = app.gameRepository.extract()
                    producer.createViewModel(game, sessionRepository)
                } else {
                    producer.createViewModel(sessionId, sessionRepository)
                }
            }
        }
    }
}

private const val TAG = "GameViewModel"
