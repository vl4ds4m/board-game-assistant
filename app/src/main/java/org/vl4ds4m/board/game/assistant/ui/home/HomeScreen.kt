package org.vl4ds4m.board.game.assistant.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.defaultGames
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    startNewGame: () -> Unit,
    proceedGame: (Long, GameType) -> Unit,
    modifier: Modifier = Modifier
) {
    HomeScreenContent(
        sessions = viewModel.sessions.collectAsState(),
        clickNewGame = startNewGame,
        clickSession = proceedGame,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    sessions: State<List<GameSessionInfo>>,
    clickNewGame: () -> Unit,
    clickSession: (Long, GameType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(clickNewGame) {
                Text("Start a new game")
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            sessions.value.sortedByDescending { it.startTime }
                .onEachIndexed { index, session ->
                    item(session.id) {
                        Text(
                            text = "${index + 1}. ${session.name}",
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .clickable { clickSession(session.id, session.type) }
                        )
                    }
                }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    BoardGameAssistantTheme {
        HomeScreenContent(
            sessions = remember { mutableStateOf(sessionsInfo) },
            clickNewGame = {},
            clickSession = { _, _ -> },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private val sessionsInfo: List<GameSessionInfo> = defaultGames.filter { !it.completed }
    .mapIndexed { i, s ->
        GameSessionInfo(
            id = i.inc().toLong(),
            completed = s.completed,
            type = s.type,
            name = s.name,
            startTime = s.startTime,
            stopTime = s.stopTime
        )
    }
