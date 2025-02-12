package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.game.GameScreenContent
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun CarcassonneGameScreen(
    viewModel: CarcassonneGameViewModel,
    modifier: Modifier = Modifier,
) {
    GameScreenContent(
        name = viewModel.name,
        playersState = viewModel.players.collectAsState(),
        currentPlayerIdState = viewModel.currentPlayerId.collectAsState(),
        onSelectPlayer = null,
        onAddPoints = { viewModel.addPoints(it) },
        modifier = modifier
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun CarcassonneGameScreenPreview() {
    BoardGameAssistantTheme {
        GameScreenContent(
            name = "Carcassonne",
            playersState = mutableStateOf(listOf()),
            currentPlayerIdState = mutableStateOf(null),
            onSelectPlayer = null,
            onAddPoints = {}
        )
    }
}
