package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter

@Composable
fun FreeGameScreen(
    viewModel: FreeGameViewModel,
    navActions: GameNavActions,
    modifier: Modifier = Modifier,
) {
    GameScreen(
        viewModel = viewModel,
        onNameFormat = { "$it (free)" },
        currentPlayerId = viewModel.currentPlayerId,
        onSelectPlayer = viewModel::changeCurrentPlayerId,
        masterActions = {
            ScoreCounter(
                onPointsAdd = viewModel::addPoints
            )
        },
        navActions = navActions,
        modifier = modifier
    )
}
