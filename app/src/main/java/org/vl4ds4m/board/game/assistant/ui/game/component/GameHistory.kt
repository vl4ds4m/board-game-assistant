package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.ui.game.ActionLog

/**
 * Displays a list of game actions that a game master has done.
 */
@Composable
fun GameHistory(
    players: State<Players>,
    actions: State<Actions>,
    modifier: Modifier = Modifier,
    showAction: ActionLog
) {
    val listState = rememberLazyListState()
    LaunchedEffect(actions.value) {
        actions.value.lastIndex
            .takeUnless { it == -1 }
            ?.let { listState.scrollToItem(it) }
    }
    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(actions.value) { action ->
            showAction(action, players.value)
                .split('\n')
                .forEach {
                    Text(text = it, maxLines = 1)
                }
        }
    }
}
