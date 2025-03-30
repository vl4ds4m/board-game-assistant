package org.vl4ds4m.board.game.assistant.ui.home

import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.network.RemoteSession
import org.vl4ds4m.board.game.assistant.network.SessionObserver
import org.vl4ds4m.board.game.assistant.ui.SessionViewModel

class HomeViewModel private constructor(
    sessionRepository: GameSessionRepository
) : SessionViewModel(sessionRepository) {
    override fun isMatched(session: GameSessionInfo) = !session.completed

    private val sessionObserver = SessionObserver()

    val remoteSessions: StateFlow<List<RemoteSession>> = sessionObserver.sessions

    suspend fun observeRemoteSession() {
        sessionObserver.observe()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = createFactory { HomeViewModel(it) }
    }
}
