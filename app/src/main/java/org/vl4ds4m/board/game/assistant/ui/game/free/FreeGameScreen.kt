package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter

@Composable
fun FreeGameScreen(
    viewModel: FreeGameViewModel,
    onGameComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GameScreen(
        viewModel = viewModel,
        onGameComplete = onGameComplete,
        onSelectPlayer = { viewModel.selectCurrentPlayer(it) },
        masterActions = {
            ScoreCounter(
                onPointsAdd = viewModel::addPoints
            )
        },
        modifier = modifier
    )
}
