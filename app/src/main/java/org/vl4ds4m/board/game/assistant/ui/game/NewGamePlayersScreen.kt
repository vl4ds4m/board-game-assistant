package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.data.Player
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Serializable
object NewGamePlayers

@Composable
internal fun NewGamePlayersContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(
            horizontal = 24.dp,
            vertical = 36.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "List of players",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            FloatingActionButton({}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
        if (false) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No players",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 24.dp)
            ) {
                items(10) {
                    PlayerCard(
                        player = Player(0, "Player ${it + 1}"),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button({}) {
                    Text(
                        text = "Start Game",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NewGamePlayersPreview() {
    BoardGameAssistantTheme {
        Scaffold(Modifier.fillMaxSize()) { innerPadding ->
            NewGamePlayersContent(
                Modifier.padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}
