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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.OrderedPlayers
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
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
    val players = viewModel.players.collectAsState()
    val remotePlayers = viewModel.remotePlayers.collectAsState(listOf())
    val newRemotePlayers = remember {
        derivedStateOf {
            remotePlayers.value.filterNot { remotePlayer ->
                players.value.any { (_, p) -> p.user?.netDevId == remotePlayer.netDevId }
            }
        }
    }
    PlayerSettingScreenContent(
        players = viewModel.orderedPlayers.collectAsState(),
        remotePlayers = newRemotePlayers,
        currentPlayerId = viewModel.currentPlayerId.collectAsState(),
        addPlayer = viewModel::addPlayer,
        playerSettingActions = PlayerSettingActions(
            onSelect = viewModel::changeCurrentPlayerId,
            onOrderChange = if (viewModel is OrderedGame)
                viewModel::changePlayerOrder else null,
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
    players: State<OrderedPlayers>,
    remotePlayers: State<List<User>>,
    currentPlayerId: State<Long?>,
    addPlayer: (User?, String) -> Unit,
    playerSettingActions: PlayerSettingActions,
    modifier: Modifier = Modifier
) {
    val playersInGame = players.value.filterNot { (_, p) -> p.removed }
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PlayersHead(rememberUpdatedState(playersInGame.size)) {
            addPlayer(null, it)
        }
        if (playersInGame.isEmpty()) {
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
                    items = playersInGame,
                    key = { _, (id, _) -> id }
                ) { i, (id, player) ->
                    PlayerSettingCard(
                        id = id,
                        name = player.name,
                        user = player.user?.self ?: false,
                        remote = player.user != null,
                        frozen = player.frozen,
                        selected = id == currentPlayerId.value,
                        settingActions = playerSettingActions,
                        index = i,
                        playersCount = rememberUpdatedState(playersInGame.size)
                    )
                }
            }
        }
        HorizontalDivider()
        RemotePlayersHead()
        if (remotePlayers.value.isEmpty()) {
            NoRemotePlayersLabel(Modifier.weight(1f))
        } else {
            val unboundPlayers = playersInGame
                .filter { (_, p) -> p.user == null }
                .map { (id, p) -> id to p.name }
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
                        add = { addPlayer(player, player.name) },
                        bind = {
                            playerSettingActions.onBind(it, player)
                        },
                        bindList = rememberUpdatedState(unboundPlayers)
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
                    1L to Player(null, "Abc", PlayerState(0, mapOf())),
                    2L to Player(User.Empty, "Def", PlayerState(0, mapOf())),
                    3L to Player(null, "Def", Player.Presence.FROZEN, PlayerState(0, mapOf()))
                )
            ),
            remotePlayers = rememberUpdatedState(
                listOf(
                    User(name = "Tre", netDevId = "fgdfg", self = true),
                    User(name = "Pdf", netDevId = "65hgf", self = false)
                )
            ),
            currentPlayerId = rememberUpdatedState(null),
            addPlayer = { _, _ -> },
            playerSettingActions = PlayerSettingActions.Empty,
            modifier = Modifier.fillMaxSize()
        )
    }
}
