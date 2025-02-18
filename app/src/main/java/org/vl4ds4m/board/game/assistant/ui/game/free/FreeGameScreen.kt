package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
    val currentPlayerId = rememberSaveable { mutableStateOf<Long?>(null) }
    GameScreen(
        viewModel = viewModel,
        onNameFormat = { "$it (free)" },
        currentPlayerId = currentPlayerId,
        onSelectPlayer = { currentPlayerId.value = it },
        masterActions = {
            ScoreCounter(
                onPointsAdd = {
                    val id = currentPlayerId.value
                    viewModel.addPoints(id, it)
                }
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
