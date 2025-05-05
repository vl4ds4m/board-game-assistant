package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.component.Timer
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistory
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.game.previewActions
import org.vl4ds4m.board.game.assistant.ui.game.previewPlayers
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ObserverGameScreen(
    players: State<Players>,
    gameUiFactory: State<GameUI.Factory>,
    currentPlayerId: State<Long?>,
    actions: State<Actions>,
    timer: State<Int?>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val timerEnabled = remember {
            derivedStateOf { timer.value != null }
        }
        if (timerEnabled.value) {
            Timer(
                state = timer,
                modifier = Modifier.padding(start = 32.dp)
            )
        }
        HorizontalDivider()
        PlayersRating(
            players = remember {
                derivedStateOf {
                    players.value.filterValues { it.active }
                }
            },
            currentPlayerId = currentPlayerId,
            onSelectPlayer = null,
            playerStats = gameUiFactory.value.playerStats,
            modifier = Modifier
                .weight(5f)
                .padding(horizontal = 16.dp)
        )
        HorizontalDivider()
        GameHistory(
            players = players,
            actions = actions,
            showAction = gameUiFactory.value.actionLog,
            modifier = Modifier
                .weight(3f)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.size(24.dp))
    }
}

@Preview
@Composable
private fun ObserverGameScreenPreview() {
    BoardGameAssistantTheme {
        ObserverGameScreen(
            players = rememberUpdatedState(previewPlayers),
            gameUiFactory = rememberUpdatedState(GameUI),
            currentPlayerId = rememberUpdatedState(null),
            actions = rememberUpdatedState(previewActions),
            timer = rememberUpdatedState(945)
        )
    }
}
