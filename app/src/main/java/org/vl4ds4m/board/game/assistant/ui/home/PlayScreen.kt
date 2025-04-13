package org.vl4ds4m.board.game.assistant.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import org.vl4ds4m.board.game.assistant.fakeRemoteSession
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayScreen(
    viewModel: HomeViewModel,
    startNewGame: () -> Unit,
    proceedGame: (String, GameType) -> Unit,
    observeGame: (String, String, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    PlayScreenContent(
        sessions = viewModel.localSessions.collectAsState(),
        remoteSessions = viewModel.remoteSessions.collectAsState(),
        clickNewGame = startNewGame,
        clickSession = proceedGame,
        clickRemoteGame = observeGame,
        modifier = modifier
    )
}

@Composable
fun PlayScreenContent(
    sessions: State<List<GameSessionInfo>>,
    remoteSessions: State<List<RemoteSessionInfo>>,
    clickNewGame: () -> Unit,
    clickSession: (String, GameType) -> Unit,
    clickRemoteGame: (String, String, String, Int) -> Unit,
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
        Text("Continue these")
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
        HorizontalDivider()
        Text("Connect to exists")
        LazyColumn(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            itemsIndexed(remoteSessions.value) { index, session ->
                Text(
                    text = "${index + 1}. ${session.name}",
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .clickable {
                            session.run { clickRemoteGame(id, name, ip, port) }
                        }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PlayScreenPreview() {
    BoardGameAssistantTheme {
        PlayScreenContent(
            sessions = remember { mutableStateOf(sessionsInfo) },
            remoteSessions = remember { mutableStateOf(fakeRemoteSession) },
            clickNewGame = {},
            clickSession = { _, _ -> },
            clickRemoteGame = { _, _, _, _ -> },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private val sessionsInfo: List<GameSessionInfo> = defaultGames.filter { !it.completed }
    .mapIndexed { i, s ->
        GameSessionInfo(
            id = i.inc().toString(),
            completed = s.completed,
            type = s.type,
            name = s.name,
            startTime = s.startTime,
            stopTime = s.stopTime
        )
    }
