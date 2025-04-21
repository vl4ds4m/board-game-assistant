package org.vl4ds4m.board.game.assistant.ui.play

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.defaultGames
import org.vl4ds4m.board.game.assistant.fakeRemoteSession
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.ui.component.GameSessionCard
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayScreen(
    viewModel: PlayViewModel,
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
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.play_title),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineMedium
        )
        HorizontalDivider()
        Button(
            onClick = clickNewGame,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.play_start_new_game),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        HorizontalDivider()
        Text(
            text = stringResource(R.string.play_continue_game_title),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        if (sessions.value.isEmpty()) {
            Text(
                text = stringResource(R.string.play_local_empty_list),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = sessions.value.sortedByDescending {
                        it.startTime
                    },
                    key = { _, session -> session.id }
                ) { index, session ->
                    GameSessionCard(
                        text = "${index + 1}. ${session.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                clickSession(session.id, session.type)
                            }
                    )
                }
            }
        }
        HorizontalDivider()
        Text(
            text = stringResource(R.string.play_join_game_title),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        if (remoteSessions.value.isEmpty()) {
            Text(
                text = stringResource(R.string.play_remote_empty_list),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = remoteSessions.value,
                    key = { _, session -> session.id }
                ) { index, session ->
                    GameSessionCard(
                        text = "${index + 1}. ${session.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                session.run {
                                    clickRemoteGame(id, name, ip, port)
                                }
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PlayScreenWithSessionsPreview() {
    PlayScreenPreview(sessionsInfo, fakeRemoteSession)
}

@Preview
@Composable
private fun PlayScreenNoSessionsPreview() {
    PlayScreenPreview(listOf(), listOf())
}

@Composable
private fun PlayScreenPreview(
    localSessions: List<GameSessionInfo>,
    remoteSession: List<RemoteSessionInfo>
) {
    BoardGameAssistantTheme {
        PlayScreenContent(
            sessions = remember { mutableStateOf(localSessions) },
            remoteSessions = remember { mutableStateOf(remoteSession) },
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
