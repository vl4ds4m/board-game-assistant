package org.vl4ds4m.board.game.assistant.ui.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ResultsContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text("No content")
    }
}

@Preview
@Composable
private fun ResultsContentPreview() {
    BoardGameAssistantTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            ResultsContent(
                Modifier.padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}
