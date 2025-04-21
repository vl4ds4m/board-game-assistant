package org.vl4ds4m.board.game.assistant.ui.game.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.network.NetworkPlayer
import org.vl4ds4m.board.game.assistant.ui.component.NewRemotePlayerCard
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayerSettingScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>()
    val onPlayerOrderChange: ((Long, Int) -> Unit)?
    val players = viewModel.players.let { flow ->
        if (viewModel is OrderedGame) {
            onPlayerOrderChange = viewModel::changePlayerOrder
            viewModel.orderedPlayerIds.combine(flow) { ids, players ->
                buildList {
                    for (id in ids) {
                        val player = players[id] ?: continue
                        add(id to player)
                    }
                }
            }
        } else {
            onPlayerOrderChange = null
            flow.map { it.toList() }
        }
    }.collectAsState(listOf())
    PlayerSettingScreenContent(
        players = players,
        remotePlayers = viewModel.remotePlayers.collectAsState(),
        userPlayer = viewModel.userPlayer.collectAsState(),
        currentPlayerId = viewModel.currentPlayerId.collectAsState(),
        addPlayer = viewModel::addPlayer,
        bindPlayer = viewModel::bindPlayer,
        onPlayerOrderChange = onPlayerOrderChange,
        playerSettingActions = PlayerSettingActions(
            onSelect = viewModel::changeCurrentPlayerId,
            onBind = viewModel::bindPlayer,
            onUnbind = viewModel::unbindPlayer,
            onRename = viewModel::renamePlayer,
            onRemove = viewModel::removePlayer,
            onFreeze = viewModel::freezePlayer,
            onUnfreeze = viewModel::unfreezePlayer
        ),
        modifier = modifier
    )
}

@Composable
fun PlayerSettingScreenContent(
    players: State<List<Pair<Long, Player>>>,
    remotePlayers: State<List<NetworkPlayer>>,
    userPlayer: State<NetworkPlayer?>,
    currentPlayerId: State<Long?>,
    addPlayer: (String?, String) -> Unit,
    bindPlayer: (Long, String) -> Unit,
    onPlayerOrderChange: ((Long, Int) -> Unit)?,
    playerSettingActions: PlayerSettingActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val count = remember { derivedStateOf { players.value.size } }
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.game_players_list),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            val playerPrefix = stringResource(R.string.game_player_prefix)
            FloatingActionButton(
                onClick = {
                    addPlayer(null, "$playerPrefix ${count.value + 1}")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add a player"
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = players.value,
                key = { _, (id, _) -> id }
            ) { i, (id, player) ->
                PlayerSettingCard(
                    id = id,
                    name = player.name,
                    remote = player.netDevId != null,
                    active = player.active,
                    selected = id == currentPlayerId.value,
                    menuActions = playerSettingActions,
                    index = i,
                    count = count,
                    onOrderChange = onPlayerOrderChange
                )
            }
        }
        HorizontalDivider()
        Text(
            text = stringResource(R.string.game_online_players),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        val newRemotePlayers = remember {
            derivedStateOf {
                remotePlayers.value.filterNot { newPlayer ->
                    players.value.any { (_, p) -> p.netDevId == newPlayer.netDevId }
                }
            }
        }
        val newUserPlayer = remember {
            derivedStateOf {
                userPlayer.value?.takeUnless { user ->
                    players.value.any { (_, p) -> p.netDevId == user.netDevId }
                }
            }
        }
        if (newRemotePlayers.value.isEmpty() && newUserPlayer.value == null) {
            Text(
                text = stringResource(R.string.game_online_players_empty),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentSize()
            )
        } else {
            val unboundPlayers = remember {
                derivedStateOf {
                    players.value.filter { (_, p) ->
                        p.netDevId == null
                    }.map { (id, p) ->
                        id to p.name
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                newUserPlayer.value?.let { user ->
                    item {
                        NewRemotePlayerCard(
                            name = "${user.name} (${stringResource(R.string.game_player_self_label)})",
                            add = {
                                user.run { addPlayer(name, netDevId) }
                            },
                            bind = { bindPlayer(it, user.netDevId) },
                            bindList = unboundPlayers
                        )
                    }
                }
                items(newRemotePlayers.value) { player ->
                    NewRemotePlayerCard(
                        name = player.name,
                        add = {
                            player.run { addPlayer(netDevId, name) }
                        },
                        bind = { bindPlayer(it, player.netDevId) },
                        bindList = unboundPlayers
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PlayerSettingScreenPreview() {
    BoardGameAssistantTheme {
        PlayerSettingScreenContent(
            players = rememberUpdatedState(
                listOf(
                    1L to Player("f", "Abc", true, PlayerState(0, mapOf())),
                    2L to Player(null, "Def", true, PlayerState(0, mapOf())),
                    3L to Player("rte", "Def", false, PlayerState(0, mapOf())),
                )
            ),
            remotePlayers = rememberUpdatedState(
                listOf(
                    NetworkPlayer(name = "Tre", netDevId = "fgdfg"),
                    NetworkPlayer(name = "Pdf", netDevId = "65hgf"),
                )
            ),
            userPlayer = rememberUpdatedState(
                NetworkPlayer(name = "Oreo", netDevId = "oi32j")
            ),
            currentPlayerId = rememberUpdatedState(null),
            addPlayer = { _, _ -> },
            bindPlayer = { _, _ -> },
            onPlayerOrderChange = { _, _ -> },
            playerSettingActions = PlayerSettingActions.Empty,
            modifier = Modifier.fillMaxSize()
        )
    }
}
