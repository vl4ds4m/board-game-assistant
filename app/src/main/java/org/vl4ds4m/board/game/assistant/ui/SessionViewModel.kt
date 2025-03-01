package org.vl4ds4m.board.game.assistant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
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

abstract class SessionViewModel(sessionRepository: GameSessionRepository) : ViewModel() {
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
            crossinline create: (GameSessionRepository) -> VM
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer<VM> {
                get(APPLICATION_KEY)
                    .let { it as BoardGameAssistantApp }
                    .let { create(it.sessionRepository) }
            }
        }
    }
}