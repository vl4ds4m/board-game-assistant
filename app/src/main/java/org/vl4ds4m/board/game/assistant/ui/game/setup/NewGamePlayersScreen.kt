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
import kotlinx.coroutines.flow.map
import org.vl4ds4m.board.game.assistant.R
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
    val remotePlayers = gameViewModel.remotePlayers.map { list ->
        list.map {
            NewPlayer(
                name = it.name,
                netDevId = it.netDevId
            )
        }
    }.collectAsState(listOf())
    val userPlayer = gameViewModel.userPlayer.map {
        it?.let {
            NewPlayer(name = it.name, netDevId = it.netDevId)
        }
    }.collectAsState(null)
    NewGamePlayersScreenContent(
        players = viewModel.players,
        remotePlayers = remotePlayers,
        userPlayer = userPlayer,
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
    remotePlayers: State<List<NewPlayer>>,
    userPlayer: State<NewPlayer?>,
    addPlayer: (String, String?) -> Unit,
    setupActions: PlayerSetupActions,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    PlayersHead { addPlayer(it, null) }
    if (players.isEmpty()) {
        NoPlayersLabel(Modifier.weight(2f))
    } else {
        val playersCount = rememberUpdatedState(players.size)
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
                    remote = player.netDevId != null,
                    setupActions = setupActions,
                    playersCount = playersCount
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
    val newRemotePlayers = remember {
        derivedStateOf {
            remotePlayers.value.filterNot { newPlayer ->
                players.any { it.netDevId == newPlayer.netDevId }
            }
        }
    }
    val newUserPlayer = remember {
        derivedStateOf {
            userPlayer.value?.takeUnless { user ->
                players.any { it.netDevId == user.netDevId }
            }
        }
    }
    if (newRemotePlayers.value.isEmpty() && newUserPlayer.value == null) {
        NoRemotePlayersLabel(Modifier.weight(1f))
    } else {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            newUserPlayer.value?.let {
                item {
                    RemotePlayerCard(
                        name = "${it.name} (${stringResource(R.string.game_player_self_label)})",
                        add = {
                            it.run { addPlayer(name, netDevId) }
                        },
                        bind = null,
                        bindList = null
                    )
                }
            }
            items(newRemotePlayers.value) { player ->
                RemotePlayerCard(
                    name = player.name,
                    add = {
                        player.run { addPlayer(name, netDevId) }
                    },
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
    NewGamePlayersScreenPreview(previewPlayers, previewRemotePlayers)
}

@Preview
@Composable
private fun NewGameWithoutPlayersScreenPreview() {
    NewGamePlayersScreenPreview(listOf(), listOf())
}

private val previewPlayers get() = listOf(
    NewPlayer("Player A", null),
    NewPlayer("Player B", "456"),
    NewPlayer("Player C", null),
)

private val previewRemotePlayers get() = listOf(
    NewPlayer("Remote Player A", "123"),
    NewPlayer("Remote Player B", "456"),
    NewPlayer("Remote Player C", "789"),
)

@Composable
private fun NewGamePlayersScreenPreview(
    players: List<NewPlayer>,
    remotePlayers: List<NewPlayer>
) {
    BoardGameAssistantTheme {
        NewGamePlayersScreenContent(
            players = players,
            remotePlayers = rememberUpdatedState(remotePlayers),
            userPlayer = rememberUpdatedState(NewPlayer("Oreo", "oi32j")),
            addPlayer = { _, _ -> },
            setupActions = PlayerSetupActions.Empty,
            onStartGame = {}
        )
    }
}
