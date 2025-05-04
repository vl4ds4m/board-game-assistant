package org.vl4ds4m.board.game.assistant.ui.results

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.ui.SessionViewModel

class ResultsViewModel private constructor(
    app: BoardGameAssistantApp
) : SessionViewModel(app) {
    override fun isMatched(session: GameSessionInfo) = session.completed

    suspend fun getSession(id: String): GameSession? = sessionRepository.loadSession(id)

    val userId: StateFlow<String?> = app.userDataRepository.netDevId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    companion object {
        val Factory: ViewModelProvider.Factory = createFactory { ResultsViewModel(it) }
    }
}
