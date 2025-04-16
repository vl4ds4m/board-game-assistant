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
import org.vl4ds4m.board.game.assistant.game.log.CurrentPlayerChangeAction
import org.vl4ds4m.board.game.assistant.game.log.PlayerStateChangeAction

@Composable
fun GameHistory(
    players: State<Players>,
    actions: State<Actions>,
    modifier: Modifier = Modifier
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
            when (action) { // TODO Implement Game specific log
                is CurrentPlayerChangeAction -> {
                    val old = getPlayerName(players.value, action.oldPlayerId)
                    val new = getPlayerName(players.value, action.newPlayerId)
                    Text("$old -> $new")
                }
                is PlayerStateChangeAction -> {
                    val name = getPlayerName(players.value, action.playerId)
                    val scoreChange = action.newState.score.let {
                        it - action.oldState.score
                    }.let {
                        if (it == 0) "no score change"
                        else if (it > 0) "+$it point(s)"
                        else "-$it point(s)"
                    }
                    Text("$name: $scoreChange")
                }
            }
        }
    }
}

private fun getPlayerName(players: Players, id: Long?): String {
    id ?: return "[nobody]"
    return players[id]?.name ?: "[removed]"
}
