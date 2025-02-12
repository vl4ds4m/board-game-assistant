package org.vl4ds4m.board.game.assistant.ui.game.free

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.game.GameScreenContent
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun FreeGameScreen(
    viewModel: FreeGameViewModel,
    modifier: Modifier = Modifier,
) {
    GameScreenContent(
        name = viewModel.name,
        playersState = viewModel.players.collectAsState(),
        currentPlayerIdState = viewModel.currentPlayerId,
        onSelectPlayer = { viewModel.selectCurrentPlayer(it) },
        onAddScore = { viewModel.addScore(it) },
        modifier = modifier
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun FreeGameScreenPreview() {
    BoardGameAssistantTheme {
        GameScreenContent(
            name = "Free game",
            playersState = mutableStateOf(listOf()),
            currentPlayerIdState = mutableStateOf(null),
            onSelectPlayer = null,
            onAddScore = {}
        )
    }
}
