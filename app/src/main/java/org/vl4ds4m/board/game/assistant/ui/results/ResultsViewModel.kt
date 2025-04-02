package org.vl4ds4m.board.game.assistant.ui.results

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.ui.SessionViewModel

class ResultsViewModel private constructor(
    extras: CreationExtras
) : SessionViewModel(extras) {
    override fun isMatched(session: GameSessionInfo) = session.completed

    suspend fun getSession(id: Long): GameSession? = sessionRepository.loadSession(id)

    companion object {
        val Factory: ViewModelProvider.Factory = createFactory { ResultsViewModel(it) }
    }
}
