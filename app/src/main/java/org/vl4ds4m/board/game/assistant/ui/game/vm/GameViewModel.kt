package org.vl4ds4m.board.game.assistant.ui.game.vm

import android.net.nsd.NsdManager
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
import org.vl4ds4m.board.game.assistant.network.SessionEmitter
import java.util.UUID

abstract class GameViewModel(
    private val gameEnv: GameEnv,
    private val sessionId: String?,
    extras: CreationExtras
) : ViewModel(), Game by gameEnv {
    private val sessionRepository: GameSessionRepository

    private val sessionEmitter: SessionEmitter

    private val gameEmitter: GameEmitter

    init {
        val app = extras[APPLICATION_KEY]
            .let { it as BoardGameAssistantApp }
        sessionRepository = app.sessionRepository
        sessionEmitter = SessionEmitter(
            app.applicationContext.getSystemService(NsdManager::class.java)
        )
        gameEmitter = GameEmitter(gameEnv, viewModelScope, sessionEmitter)
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
            gameEmitter.startEmit("${sessionId ?: 0L}", gameEnv.name.value)
        }
        gameEnv.initialize()
    }

    override fun onCleared() {
        if (initialized.value) {
            Log.d(TAG, "Complete game process")
            gameEnv.initializables.forEach { it.close() }
            val id = sessionId ?: UUID.randomUUID().toString() // TODO CREATE BEFORE NET EXPOSURE
            gameEnv.save()
                .let { sessionRepository.saveSession(it, id) }
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
