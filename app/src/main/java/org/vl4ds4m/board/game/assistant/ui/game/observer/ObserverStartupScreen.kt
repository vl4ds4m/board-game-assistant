package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ObserverStartupScreen(modifier: Modifier = Modifier) {
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
        ObserverStartupScreen()
    }
}
