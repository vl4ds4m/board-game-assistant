package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ObserverEndScreen(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GameScreen(
        topBarTitle = title,
        onBackClick = onBackClick,
        modifier = modifier
    ) { innerModifier ->
        Box(
            modifier = innerModifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("End of game")
        }
    }
}

@Preview
@Composable
private fun ObserverEndScreenPreview() {
    BoardGameAssistantTheme {
        ObserverEndScreen(
            title = "Some game",
            onBackClick = {}
        )
    }
}