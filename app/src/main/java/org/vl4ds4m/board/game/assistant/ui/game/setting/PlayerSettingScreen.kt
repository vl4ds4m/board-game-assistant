package org.vl4ds4m.board.game.assistant.ui.game.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.NoPlayersLabel
import org.vl4ds4m.board.game.assistant.ui.game.component.NoRemotePlayersLabel
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersHead
import org.vl4ds4m.board.game.assistant.ui.game.component.RemotePlayerCard
import org.vl4ds4m.board.game.assistant.ui.game.component.RemotePlayersHead
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
        playerSettingActions = PlayerSettingActions(
            onSelect = viewModel::changeCurrentPlayerId,
            onOrderChange = onPlayerOrderChange,
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
    playerSettingActions: PlayerSettingActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PlayersHead { addPlayer(null, it) }
        if (players.value.isEmpty()) {
            NoPlayersLabel(Modifier.weight(2f))
        } else {
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
                        frozen = !player.active,
                        selected = id == currentPlayerId.value,
                        settingActions = playerSettingActions,
                        index = i,
                        playersCount = remember {
                            derivedStateOf { players.value.size }
                        }
                    )
                }
            }
        }
        HorizontalDivider()
        RemotePlayersHead()
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
            NoRemotePlayersLabel(Modifier.weight(1f))
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
                        RemotePlayerCard(
                            name = "${user.name} (${stringResource(R.string.game_player_self_label)})",
                            add = {
                                user.run { addPlayer(netDevId, name) }
                            },
                            bind = {
                                playerSettingActions.onBind(it, user.netDevId)
                            },
                            bindList = unboundPlayers
                        )
                    }
                }
                items(newRemotePlayers.value) { player ->
                    RemotePlayerCard(
                        name = player.name,
                        add = {
                            player.run { addPlayer(netDevId, name) }
                        },
                        bind = {
                            playerSettingActions.onBind(it, player.netDevId)
                        },
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
            playerSettingActions = PlayerSettingActions.Empty,
            modifier = Modifier.fillMaxSize()
        )
    }
}
