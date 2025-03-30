package org.vl4ds4m.board.game.assistant.ui.home

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.network.SessionObserver
import org.vl4ds4m.board.game.assistant.ui.SessionViewModel

class HomeViewModel private constructor(
    sessionRepository: GameSessionRepository
) : SessionViewModel(sessionRepository) {
    override fun isMatched(session: GameSessionInfo) = !session.completed

    private val sessionObserver = SessionObserver(viewModelScope)

    val remoteSessions: StateFlow<List<RemoteSessionInfo>> = sessionObserver.sessions

    init {
        sessionObserver.startObserve()
    }

    override fun onCleared() {
        super.onCleared()
        sessionObserver.stopObserve()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = createFactory { HomeViewModel(it) }
    }
}
