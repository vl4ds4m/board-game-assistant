package org.vl4ds4m.board.game.assistant.ui.results

import androidx.lifecycle.ViewModelProvider
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.ui.SessionViewModel

class ResultsViewModel private constructor(
    app: BoardGameAssistantApp
) : SessionViewModel(app) {
    override fun isMatched(session: GameSessionInfo) = session.completed

    suspend fun getSession(id: String): GameSession? = sessionRepository.loadSession(id)

    fun removeSession(id: String) {
        sessionRepository.removeSession(id)
    }

    val typeFilters: Filters<GameType> = Filters(GameType.entries)

    companion object {
        val Factory: ViewModelProvider.Factory = createFactory { ResultsViewModel(it) }
    }
}
