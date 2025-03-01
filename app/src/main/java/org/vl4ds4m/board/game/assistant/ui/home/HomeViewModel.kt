package org.vl4ds4m.board.game.assistant.ui.home

import androidx.lifecycle.ViewModelProvider
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.ui.SessionViewModel

class HomeViewModel private constructor(
    sessionRepository: GameSessionRepository
) : SessionViewModel(sessionRepository) {
    override fun isMatched(session: GameSessionInfo) = !session.completed

    companion object {
        val Factory: ViewModelProvider.Factory = createFactory { HomeViewModel(it) }
    }
}
