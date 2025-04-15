package org.vl4ds4m.board.game.assistant.ui.game.dice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

@Composable
fun DiceGameScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>() as DiceGameViewModel
    OrderedGameScreen(modifier) {
        ScoreCounter(
            onPointsAdd = viewModel::addPoints
        )
    }
}
