package org.vl4ds4m.board.game.assistant.ui.game.ordered

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

@Composable
fun SimpleOrderedGameScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>() as SimpleOrderedGameViewModel
    OrderedGameScreen(modifier) {
        ScoreCounter(
            onPointsAdd = viewModel::addPoints
        )
    }
}
