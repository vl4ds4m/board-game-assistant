package org.vl4ds4m.board.game.assistant.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun HomeScreen(
    onStartNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sessions = remember { Store.sessions }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onStartNewGame) {
                Text("Start a new game")
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            items(
                items = sessions,
                key = { it.id }
            ) {
                Text(
                    text = "${it.name} ${it.id}",
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    BoardGameAssistantTheme {
        Scaffold { innerPadding ->
            HomeScreen(
                onStartNewGame = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
