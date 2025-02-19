package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.GameScreenPreview
import org.vl4ds4m.board.game.assistant.ui.game.component.GameMenuActions
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter

@Composable
fun FreeGameScreen(
    viewModel: FreeGameViewModel,
    menuActions: GameMenuActions,
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
        menuActions = menuActions,
        modifier = modifier
    )
}

@Preview
@Composable
private fun FreeGameScreenPreview() {
    GameScreenPreview(
        masterActions = {
            ScoreCounter(
                onPointsAdd = {}
            )
        }
    )
}
