package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen

@Composable
fun CarcassonneGameScreen(
    viewModel: CarcassonneGameViewModel,
    navActions: GameNavActions,
    modifier: Modifier = Modifier,
) {
    OrderedGameScreen(
        viewModel = viewModel,
        onNameFormat = { "Carcassonne '$it'" },
        masterActions = {
            CarcassonneCounter(
                onPointsAdd = viewModel::addPoints,
                onSkip = viewModel::skip,
                onFinal = viewModel.onFinal.collectAsState(),
                onFinalChange = { viewModel.onFinal.value = it }
            )
        },
        navActions = navActions,
        modifier = modifier
    )
}
