package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.GameScreenContent
import org.vl4ds4m.board.game.assistant.ui.game.ScoreCounter

@Composable
fun FreeGameScreen(
    viewModel: FreeGameViewModel,
    modifier: Modifier = Modifier,
) {
    GameScreenContent(
        name = viewModel.name,
        players = viewModel.players.collectAsState(),
        currentPlayerId = viewModel.currentPlayerId,
        onSelectPlayer = { viewModel.selectCurrentPlayer(it) },
        masterActions = {
            ScoreCounter(
                onAddPoints = { viewModel.addPoints(it) }
            )
        },
        modifier = modifier
    )
}
