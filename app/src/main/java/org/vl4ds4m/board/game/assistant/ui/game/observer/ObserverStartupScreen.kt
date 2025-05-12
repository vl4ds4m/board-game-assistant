package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

/**
 * Displays a message to wait for the game session start.
 */
@Composable
fun ObserverStartupScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.observe_registr_msg))
    }
}

@Preview
@Composable
private fun ObserverStartupScreenPreview() {
    BoardGameAssistantTheme {
        ObserverStartupScreen()
    }
}
