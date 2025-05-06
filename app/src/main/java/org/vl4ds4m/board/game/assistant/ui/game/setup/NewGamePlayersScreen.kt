package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.NoPlayersLabel
import org.vl4ds4m.board.game.assistant.ui.game.component.NoRemotePlayersLabel
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersHead
import org.vl4ds4m.board.game.assistant.ui.game.component.RemotePlayerCard
import org.vl4ds4m.board.game.assistant.ui.game.component.RemotePlayersHead
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun NewGamePlayersScreen(
    viewModel: GameSetupViewModel,
    gameViewModel: GameViewModel,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val players = viewModel.players
    val remotePlayers = gameViewModel.remotePlayers.collectAsState(listOf())
    val newRemotePlayers = remember {
        derivedStateOf {
            remotePlayers.value.filterNot { remotePlayer ->
                players.any { it.user?.netDevId == remotePlayer.netDevId }
            }
        }
    }
    NewGamePlayersScreenContent(
        players = players,
        remotePlayers = newRemotePlayers,
        addPlayer = viewModel::addPlayer,
        setupActions = PlayerSetupActions(
            onRename = viewModel::renamePlayer,
            onRemove = viewModel::removePlayerAt,
            onOrderChange = viewModel::changePlayerOrder
        ),
        onStartGame = onStartGame,
        modifier = modifier
    )
}

@Composable
fun NewGamePlayersScreenContent(
    players: List<NewPlayer>,
    remotePlayers: State<List<User>>,
    addPlayer: (String, User?) -> Unit,
    setupActions: PlayerSetupActions,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    PlayersHead(rememberUpdatedState(players.size)) {
        addPlayer(it, null)
    }
    if (players.isEmpty()) {
        NoPlayersLabel(Modifier.weight(2f))
    } else {
        LazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(players) { index, player ->
                PlayerSetupCard(
                    index = index,
                    name = player.name,
                    user = player.user?.self ?: false,
                    remote = player.user != null,
                    setupActions = setupActions,
                    playersCount = remember {
                        derivedStateOf { players.size }
                    }
                )
            }
        }
        Button(
            onClick = onStartGame,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.new_game_start),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
    HorizontalDivider()
    RemotePlayersHead()
    if (remotePlayers.value.isEmpty()) {
        NoRemotePlayersLabel(Modifier.weight(1f))
    } else {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(remotePlayers.value) { player ->
                RemotePlayerCard(
                    name = player.name,
                    user = player.self,
                    add = { addPlayer(player.name, player) },
                    bind = null,
                    bindList = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewGameWithPlayersScreenPreview() {
    val previewPlayers = listOf(
        NewPlayer("Player A", null),
        NewPlayer("Player B", null),
        NewPlayer("Oreo", User.Empty),
        NewPlayer("Player C", null),
    )
    val previewRemotePlayers = listOf(
        User(name = "Remote Player A", netDevId = "123", self = false),
        User(name = "Remote Player B", netDevId = "456", self = false),
        User(name = "Remote Player C", netDevId = "789", self = true),
    )
    NewGamePlayersScreenPreview(previewPlayers, previewRemotePlayers)
}

@Preview
@Composable
private fun NewGameWithoutPlayersScreenPreview() {
    NewGamePlayersScreenPreview(listOf(), listOf())
}

@Composable
private fun NewGamePlayersScreenPreview(
    players: List<NewPlayer>,
    remotePlayers: List<User>
) {
    BoardGameAssistantTheme {
        NewGamePlayersScreenContent(
            players = players,
            remotePlayers = rememberUpdatedState(remotePlayers),
            addPlayer = { _, _ -> },
            setupActions = PlayerSetupActions.Empty,
            onStartGame = {}
        )
    }
}
