package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.data.repository.UserDataRepository
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.network.GameObserver
import org.vl4ds4m.board.game.assistant.network.NetworkGameState
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo

class GameObserverViewModel(
    sessionInfo: RemoteSessionInfo,
    userDataRepository: UserDataRepository,
    private val sessionRepository: GameSessionRepository
) : ViewModel() {
    private val sessionId: String = sessionInfo.id

    private val observer = GameObserver(userDataRepository, viewModelScope)

    val observerState: StateFlow<NetworkGameState> = observer.observerState

    val sessionState: StateFlow<GameSession?> = observer.sessionState
        .combine(userDataRepository.netDevId) { session, netDevId ->
            session?.changeCurrentUser(netDevId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        observer.startObserve(sessionInfo)
    }

    override fun onCleared() {
        super.onCleared()
        sessionState.value?.let {
            sessionRepository.saveSession(it, sessionId)
        }
        observer.stopObserve()
    }

    companion object {
        fun createFactory(
            sessionInfo: RemoteSessionInfo
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer<GameObserverViewModel> {
                val app = BoardGameAssistantApp.from(this)
                GameObserverViewModel(
                    sessionInfo,
                    app.userDataRepository,
                    app.sessionRepository
                )
            }
        }
    }
}
