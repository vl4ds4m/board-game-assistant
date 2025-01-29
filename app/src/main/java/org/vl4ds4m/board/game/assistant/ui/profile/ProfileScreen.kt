package org.vl4ds4m.board.game.assistant.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Serializable
object Profile

@Composable
internal fun ProfileContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text("No content")
    }
}

@Preview
@Composable
private fun ProfileContentPreview() {
    BoardGameAssistantTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            ProfileContent(
                Modifier.padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}
