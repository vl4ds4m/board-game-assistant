package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ObserverStartupScreen(
    viewModel: GameObserverViewModel,
    onBackClick: () -> Unit,
    //enterGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsState()
    val title = getGameTitle(state.value.name, state.value.type)
    GameScreen(
        topBarTitle = title,
        onBackClick = onBackClick,
        modifier = modifier
    ) { innerModifier ->
        ObserverStartupScreenContent(innerModifier)
    }
}

@Composable
fun ObserverStartupScreenContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Wait for game master start the game")
    }
}

@Preview
@Composable
private fun ObserverStartupScreenPreview() {
    BoardGameAssistantTheme {
        GameScreen(
            topBarTitle = "Some game",
            onBackClick = {},
        ) { innerModifier ->
            ObserverStartupScreenContent(innerModifier)
        }
    }
}
