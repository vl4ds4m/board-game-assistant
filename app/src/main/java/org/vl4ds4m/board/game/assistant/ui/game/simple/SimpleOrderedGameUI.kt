package org.vl4ds4m.board.game.assistant.ui.game.simple

import androidx.compose.runtime.Composable
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.StandardCounter

class SimpleOrderedGameUI private constructor(vm: SimpleOrderedGameViewModel) :
    GameUI by GameUI.create(vm, false)
{
    override val masterActions: @Composable () -> Unit = {
        StandardCounter(
            addPoints = vm::addPoints,
            applyEnabled = null,
            selectNextPlayer = vm::changeCurrentPlayerId
        )
    }

    companion object : GameUI.Factory {
        override fun create(viewModel: GameViewModel): GameUI =
            SimpleOrderedGameUI(viewModel as SimpleOrderedGameViewModel)
    }
}
