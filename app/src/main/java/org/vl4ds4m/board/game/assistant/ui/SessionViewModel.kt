package org.vl4ds4m.board.game.assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo

abstract class SessionViewModel(app: BoardGameAssistantApp) : ViewModel() {
    val sessionRepository: GameSessionRepository = app.sessionRepository

    val sessions: StateFlow<List<GameSessionInfo>> by lazy {
        sessionRepository.getAllSessions()
            .map {
                it.filter { session -> isMatched(session) }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())
    }

    protected abstract fun isMatched(session: GameSessionInfo): Boolean

    companion object {
        inline fun <reified VM : SessionViewModel> createFactory(
            crossinline create: (BoardGameAssistantApp) -> VM
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer<VM> {
                create(BoardGameAssistantApp.from(this))
            }
        }
    }
}