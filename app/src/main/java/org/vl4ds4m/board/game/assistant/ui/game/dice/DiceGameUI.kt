package org.vl4ds4m.board.game.assistant.ui.game.dice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter

class DiceGameUI private constructor(vm: DiceGameViewModel) :
    GameUI by GameUI.create(vm, false)
{
    override val masterActions: @Composable () -> Unit = {
        ScoreCounter(
            addPoints = vm::addPoints,
            applyEnabled = vm.addEnabled.collectAsState(),
            selectNextPlayer = vm::changeCurrentPlayerId,
            pointsVariants = listOf(5, 10, 50)
        )
    }

    companion object : GameUI.Factory {
        override fun create(viewModel: GameViewModel): GameUI =
            DiceGameUI(viewModel as DiceGameViewModel)
    }
}
