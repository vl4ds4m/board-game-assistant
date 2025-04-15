package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

@Composable
fun CarcassonneGameScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>() as CarcassonneGameViewModel
    OrderedGameScreen(modifier) {
        CarcassonneCounter(
            onPointsAdd = viewModel::addPoints,
            onSkip = viewModel::skip,
            onFinal = viewModel.onFinal.collectAsState(),
            onFinalChange = { viewModel.onFinal.value = it }
        )
    }
}
