package org.vl4ds4m.board.game.assistant.ui.game.vm

import androidx.lifecycle.ViewModelProvider
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.game.Game

interface GameViewModelProducer<out VM : GameViewModel> {
    fun createViewModel(game: Game, sessionRepository: GameSessionRepository): VM

    fun createViewModel(sessionId: Long, sessionRepository: GameSessionRepository): VM

    fun createFactory(sessionId: Long?): ViewModelProvider.Factory =
        GameViewModel.createFactory(sessionId, this)
}
