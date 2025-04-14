package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.ui.component.NewPlayerCard
import org.vl4ds4m.board.game.assistant.ui.component.NewRemotePlayerCard
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun NewGamePlayersScreen(
    viewModel: GameSetupViewModel,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NewGamePlayersScreenContent(
        players = viewModel.players,
        remotePlayers = viewModel.remotePlayers,
        addPlayer = viewModel::addPlayer,
        renamePlayer = viewModel::renamePlayer,
        removePlayer = viewModel::removePlayerAt,
        movePlayerUp = viewModel::movePlayerUp,
        movePlayerDown = viewModel::movePlayerDown,
        onStartGame = onStartGame,
        modifier = modifier
    )
}

@Composable
fun NewGamePlayersScreenContent(
    players: List<NewPlayer>,
    remotePlayers: List<NewPlayer>,
    addPlayer: (String, String?) -> Unit,
    renamePlayer: (Int, String) -> Unit,
    removePlayer: (Int) -> Unit,
    movePlayerUp: (Int) -> Unit,
    movePlayerDown: (Int) -> Unit,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    Row(
        modifier = Modifier.padding(16.dp),
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
            onClick = {
                addPlayer("Player ${players.size + 1}", null)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add a player"
            )
        }
    }
    if (players.isEmpty()) {
        Text(
            text = "No players",
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .wrapContentSize()
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(players) { index, player ->
                NewPlayerCard(
                    name = player.name,
                    remote = player.netDevId != null,
                    editName = { renamePlayer(index, it) },
                    remove = { removePlayer(index) },
                    moveUp = { movePlayerUp(index) }
                        .takeIf { index != 0 },
                    moveDown = { movePlayerDown(index) }
                        .takeIf { index != players.lastIndex }
                )
            }
        }
        Button(
            onClick = onStartGame,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Start the game",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
    HorizontalDivider()
    Text(
        text = "Add online players",
        modifier = Modifier.padding(start = 16.dp),
        style = MaterialTheme.typography.titleMedium
    )
    if (remotePlayers.isEmpty()) {
        Text(
            text = "No online players",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .wrapContentSize()
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(remotePlayers) { player ->
                NewRemotePlayerCard(
                    name = player.name,
                    add = {
                        player.run { addPlayer(name, netDevId) }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewGameWithPlayersScreenPreview() {
    NewGamePlayersScreenPreview(previewPlayers, previewRemotePlayers)
}

@Preview
@Composable
private fun NewGameWithoutPlayersScreenPreview() {
    NewGamePlayersScreenPreview(listOf(), listOf())
}

private val previewPlayers get() = listOf(
    NewPlayer("Abc", null),
    NewPlayer("Sdg44", "sdf5"),
    NewPlayer("Def", null),
)

private val previewRemotePlayers get() = listOf(
    NewPlayer("Abc", "123"),
    NewPlayer("Def", "456"),
)

@Composable
private fun NewGamePlayersScreenPreview(
    players: List<NewPlayer>,
    remotePlayers: List<NewPlayer>
) {
    BoardGameAssistantTheme {
        NewGamePlayersScreenContent(
            players = players,
            remotePlayers = remotePlayers,
            addPlayer = { _, _ -> },
            renamePlayer = { _, _ -> },
            removePlayer = {},
            movePlayerUp = {},
            movePlayerDown = {},
            onStartGame = {}
        )
    }
}
