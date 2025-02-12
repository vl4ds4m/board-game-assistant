package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.GameScreenContent
import org.vl4ds4m.board.game.assistant.ui.game.ScoreCounter

@Composable
fun MonopolyGameScreen(
    viewModel: MonopolyGameViewModel,
    modifier: Modifier = Modifier,
) {
    GameScreenContent(
        name = viewModel.name,
        players = viewModel.players.collectAsState(),
        currentPlayerId = viewModel.currentPlayerId.collectAsState(),
        onSelectPlayer = null,
        masterActions = {
            ScoreCounter(
                onAddPoints = { viewModel.addPoints(it) }
            )
        },
        modifier = modifier
    )
}
