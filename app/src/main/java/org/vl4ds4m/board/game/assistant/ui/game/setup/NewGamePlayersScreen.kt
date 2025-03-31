package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayerCard
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun NewGamePlayersScreen(
    viewModel: GameSetupViewModel,
    onBackClick: () -> Unit,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GameScreen(
        topBarTitle = "New game",
        onBackClick = onBackClick,
        modifier = modifier
    ) { innerModifier ->
        NewGamePlayersScreenContent(
            players = viewModel.players,
            onAddPlayer = viewModel::addPlayer,
            onRenamePlayer = viewModel::renamePlayer,
            onRemovePlayer = viewModel::removePlayerAt,
            onStartGame = onStartGame,
            modifier = innerModifier
        )
    }
}

@Composable
fun NewGamePlayersScreenContent(
    players: List<String>,
    onAddPlayer: (String) -> Unit,
    onRenamePlayer: (Int, String) -> Unit,
    onRemovePlayer: (Int) -> Unit,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.padding(
        horizontal = 16.dp,
        vertical = 32.dp
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
        FloatingActionButton(
            onClick = { onAddPlayer("Player ${players.size + 1}") }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
    if (players.isEmpty()) {
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
                .padding(vertical = 24.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(players) { index, player ->
                PlayerCard(
                    playerName = player,
                    onPlayerNameEdited = { newName -> onRenamePlayer(index, newName) },
                    onCloseClick = { onRemovePlayer(index) }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onStartGame
            ) {
                Text(
                    text = "Start Game",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewGamePlayersScreenPreview() {
    BoardGameAssistantTheme {
        NewGamePlayersScreenContent(
            players = listOf(),
            onAddPlayer = {},
            onRenamePlayer = { _, _ ->},
            onRemovePlayer = {},
            onStartGame = {}
        )
    }
}
