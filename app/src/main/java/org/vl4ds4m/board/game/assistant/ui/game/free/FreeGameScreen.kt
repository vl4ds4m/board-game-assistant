package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter

@Composable
fun FreeGameScreen(
    viewModel: FreeGameViewModel,
    onGameComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentPlayerId = rememberSaveable { mutableStateOf<Long?>(null) }
    GameScreen(
        viewModel = viewModel,
        onGameComplete = onGameComplete,
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
        modifier = modifier
    )
}
