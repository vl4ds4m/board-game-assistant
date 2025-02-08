package org.vl4ds4m.board.game.assistant.ui.game.ordered

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.game.GameScreenContent
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun OrderedGameScreen(
    viewModel: OrderedGameViewModel,
    modifier: Modifier = Modifier,
) {
    GameScreenContent(
        name = viewModel.name,
        playersState = viewModel.players.collectAsState(),
        playerScoresState = viewModel.playerScores.collectAsState(),
        currentPlayerIdState = viewModel.currentPlayerId.collectAsState(),
        onSelectPlayer = null,
        onAddScore = { viewModel.addScore(it) },
        modifier = modifier
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun OrderedGameScreenPreview() {
    BoardGameAssistantTheme {
        GameScreenContent(
            name = "Simple game",
            playersState = mutableStateOf(listOf()),
            playerScoresState = mutableStateOf(mapOf()),
            currentPlayerIdState = mutableStateOf(null),
            onSelectPlayer = null,
            onAddScore = {}
        )
    }
}
