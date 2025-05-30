package org.vl4ds4m.board.game.assistant.ui.game

import androidx.lifecycle.ViewModelProvider
import org.vl4ds4m.board.game.assistant.BoardGameAssistantApp
import org.vl4ds4m.board.game.assistant.game.env.GameEnv

interface GameViewModelProducer<out VM : GameViewModel> {
    fun createViewModel(gameEnv: GameEnv, app: BoardGameAssistantApp): VM

    fun createViewModel(sessionId: String, app: BoardGameAssistantApp): VM

    fun createFactory(sessionId: String?): ViewModelProvider.Factory =
        GameViewModel.createFactory(sessionId, this)
}
