package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.Users
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

/**
 * Displays a list of players, sorted by their score.
 */
@Composable
fun PlayersRating(
    players: State<Players>,
    users: State<Users>,
    currentPid: State<PID?>,
    onSelectPlayer: ((PID) -> Unit)?,
    modifier: Modifier = Modifier,
    playerStats: @Composable RowScope.(State<PlayerState>) -> Unit
) {
    val rating = remember {
        derivedStateOf {
            players.value.toList()
                .filter { (_, p) -> !p.removed }
                .sortedBy { (_, p) -> p }
        }
    }
    val listState = rememberLazyListState()
    val currentIndex = remember {
        derivedStateOf {
            currentPid.value?.let { current ->
                rating.value.indexOfFirst { (id, _) -> id == current }
                    .takeUnless { it == -1 }
            }
        }
    }
    LaunchedEffect(currentIndex.value) {
        currentIndex.value?.let { listState.scrollToItem(it) }
    }
    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = rating.value,
            key = { _, (id, _) -> id }
        ) { i, (id, player) ->
            val user = users.value[id]
            val playerState = rememberUpdatedState(player.state)
            PlayerGameCard(
                position = i + 1,
                name = player.name,
                user = user?.self ?: false,
                remote = user != null,
                frozen = player.frozen,
                stats = { playerStats(playerState) },
                selected = id == currentPid.value,
                onCardSelected = onSelectPlayer?.let { select ->
                    { select(id) }
                }
            )
        }
    }
}
