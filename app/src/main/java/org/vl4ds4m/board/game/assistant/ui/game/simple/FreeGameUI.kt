package org.vl4ds4m.board.game.assistant.ui.game.simple

import androidx.compose.runtime.Composable
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.StandardCounter

class FreeGameUI private constructor(vm: FreeGameViewModel) :
    GameUI by GameUI.create(vm, true)
{
    override val masterActions: @Composable () -> Unit = {
        StandardCounter(
            addPoints = vm::addPoints,
            applyEnabled = null,
            selectNextPlayer = null
        )
    }

    companion object : GameUI.Factory {
        override fun create(viewModel: GameViewModel): GameUI =
            FreeGameUI(viewModel as FreeGameViewModel)
    }
}
