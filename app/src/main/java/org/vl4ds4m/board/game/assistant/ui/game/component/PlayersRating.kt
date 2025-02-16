package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.domain.Player
import org.vl4ds4m.board.game.assistant.ui.game.PlayerInGameCard

@Composable
fun PlayersRating(
    players: State<List<Player>>,
    currentPlayerId: State<Long?>,
    onSelectPlayer: ((Player) -> Unit)?,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(
            items = players.value,
            key = { _, player -> player.id }
        ) { i, player ->
            PlayerInGameCard(
                rating = i + 1,
                name = player.name,
                score = player.score,
                selected = player.id == currentPlayerId.value,
                onSelect = onSelectPlayer?.let { f -> { f(player) } }
            )
        }
    }
}
