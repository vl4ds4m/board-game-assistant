package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

@Composable
fun CarcassonneGameScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>() as CarcassonneGameViewModel
    OrderedGameScreen(modifier) {
        CarcassonneCounter(
            addPoints = viewModel::addPoints,
            selectNextPlayer = viewModel::changeCurrentPlayerId
        )
    }
}
