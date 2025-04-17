package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistory
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.game.component.ShowGameAction
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ObserverGameScreen(
    players: State<Players>,
    currentPlayerId: State<Long?>,
    actions: State<Actions>,
    showAction: ShowGameAction,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val activePlayers = remember {
            derivedStateOf {
                players.value.filterValues { it.active }
            }
        }
        PlayersRating(
            players = activePlayers,
            currentPlayerId = currentPlayerId,
            onSelectPlayer = null,
            modifier = Modifier.weight(1f)
        )
        GameHistory(
            players = players,
            actions = actions,
            showAction = showAction,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ObserverGameScreenPreview() {
    BoardGameAssistantTheme {
        ObserverGameScreen(
            players = remember { mutableStateOf(mapOf()) },
            currentPlayerId = remember { mutableStateOf(null) },
            actions = remember { mutableStateOf(listOf()) },
            showAction = { _, _ -> "Some action" }
        )
    }
}
