package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.domain.Player
import org.vl4ds4m.board.game.assistant.ui.game.PlayerInGameCard

@Composable
fun PlayersRating(
    players: State<Map<Long, Player>>,
    currentPlayerId: State<Long?>,
    onSelectPlayer: ((Long) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val rating = remember {
        derivedStateOf {
            players.value.toList()
                .sortedByDescending { it.second.score }
        }
    }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(
            items = rating.value,
            key = { _, (id, _) -> id }
        ) { i, (id, player) ->
            PlayerInGameCard(
                rating = i + 1,
                name = player.name,
                score = player.score,
                selected = id == currentPlayerId.value,
                onSelect = onSelectPlayer?.let { f -> { f(id) } }
            )
        }
    }
}
