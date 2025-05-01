package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import androidx.compose.runtime.Composable
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel

class CarcassonneGameUI private constructor(vm: CarcassonneGameViewModel) :
    GameUI by GameUI.create(vm, false)
{
    override val masterActions: @Composable () -> Unit = {
        CarcassonneCounter(
            addPoints = vm::addPoints,
            selectNextPlayer = vm::changeCurrentPlayerId
        )
    }

    companion object : GameUI.Factory {
        override fun create(viewModel: GameViewModel): GameUI =
            CarcassonneGameUI(viewModel as CarcassonneGameViewModel)
    }
}
