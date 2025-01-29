package org.vl4ds4m.board.game.assistant.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Serializable
object Home

@Composable
internal fun HomeContent(
    sessions: List<String>,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onStart) {
                Text("Start a new game")
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            items(sessions) {
                Text(
                    text = it,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    BoardGameAssistantTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            HomeContent(
                sessions = listOf(
                    "Example session",
                    "Carcassons 234",
                    "Monopoly 645",
                    "Carcassons 785",
                    "Dice 2234",
                    "Dice 6556"
                ),
                onStart = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
