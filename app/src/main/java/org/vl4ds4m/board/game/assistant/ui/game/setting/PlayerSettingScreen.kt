package org.vl4ds4m.board.game.assistant.ui.game.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.state.Score
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayerSettingScreen(
    viewModel: GameViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val onPlayerOrderChange: ((Long, Int) -> Unit)?
    val players = viewModel.players.let { flow ->
        if (viewModel is OrderedGameViewModel) {
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
    GameScreen(
        topBarTitle = "Player Settings",
        onBackClick = onBackClick,
        modifier = modifier
    ) { innerModifier ->
        PlayerSettingScreenContent(
            players = players,
            currentPlayerId = viewModel.currentPlayerId.collectAsState(),
            onPlayerAdd = { viewModel.addPlayer("New player") },
            onPlayerOrderChange = onPlayerOrderChange,
            playerSettingActions = PlayerSettingActions(
                onSelect = viewModel::changeCurrentPlayerId,
                onRename = viewModel::renamePlayer,
                onRemove = viewModel::removePlayer,
                onFreeze = viewModel::freezePlayer,
                onUnfreeze = viewModel::unfreezePlayer
            ),
            modifier = innerModifier
        )
    }
}

@Composable
fun PlayerSettingScreenContent(
    players: State<List<Pair<Long, Player>>>,
    currentPlayerId: State<Long?>,
    onPlayerAdd: () -> Unit,
    onPlayerOrderChange: ((Long, Int) -> Unit)?,
    playerSettingActions: PlayerSettingActions,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        val count = remember { derivedStateOf { players.value.size } }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(
                items = players.value,
                key = { _, (id, _) -> id }
            ) { i, (id, player) ->
                PlayerSettingCard(
                    id = id,
                    name = player.name,
                    active = player.active,
                    selected = id == currentPlayerId.value,
                    menuActions = playerSettingActions,
                    index = i,
                    count = count,
                    onOrderChange = onPlayerOrderChange
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        FloatingActionButton(
            onClick = onPlayerAdd,
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add player"
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun PlayerSettingScreenPreview() {
    BoardGameAssistantTheme {
        PlayerSettingScreenContent(
            players = mutableStateOf(listOf(
                1L to Player("Abc", true, Score()),
                2L to Player("Def", true, Score()),
            )),
            currentPlayerId = mutableStateOf(null),
            onPlayerAdd = {},
            onPlayerOrderChange = { _, _ -> },
            playerSettingActions = PlayerSettingActions.Empty,
            modifier = Modifier.fillMaxSize()
        )
    }
}
