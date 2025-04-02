package org.vl4ds4m.board.game.assistant.ui.game.vm

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.vl4ds4m.board.game.assistant.game.Game

interface GameViewModelProducer<out VM : GameViewModel> {
    fun createViewModel(game: Game, extras: CreationExtras): VM

    fun createViewModel(sessionId: String, extras: CreationExtras): VM

    fun createFactory(sessionId: String?): ViewModelProvider.Factory =
        GameViewModel.createFactory(sessionId, this)
}
