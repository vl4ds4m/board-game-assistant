package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistory
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.game.fakeActions
import org.vl4ds4m.board.game.assistant.ui.game.fakePlayers
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ObserverGameScreen(
    viewModel: GameObserverViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsState()
    val title = remember {
        derivedStateOf { getGameTitle(state.value.name, state.value.type) }
    }
    val players = remember {
        derivedStateOf { state.value.players }
    }
    val currentPlayerId = remember {
        derivedStateOf { state.value.currentPlayerId }
    }
    val actions = remember {
        derivedStateOf { state.value.actions }
    }
    GameScreen(
        topBarTitle = title.value,
        onBackClick = onBackClick,
        modifier = modifier
    ) { innerModifier ->
        ObserverGameScreenContent(
            players = players,
            currentPlayerId = currentPlayerId,
            actions = actions,
            modifier = innerModifier
        )
    }
}

@Composable
fun ObserverGameScreenContent(
    players: State<Players>,
    currentPlayerId: State<Long?>,
    actions: State<Actions>,
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
        GameScreen(
            topBarTitle = "Some game",
            onBackClick = {},
            modifier = Modifier.fillMaxSize(),
        ) { innerModifier ->
            ObserverGameScreenContent(
                players = remember { mutableStateOf(fakePlayers) },
                currentPlayerId = remember { mutableStateOf(1) },
                actions = remember { mutableStateOf(fakeActions) },
                modifier = innerModifier
            )
        }
    }
}

fun getGameTitle(name: String, type: GameType): String = "$name (${type.title})"
