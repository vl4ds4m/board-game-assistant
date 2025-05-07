package org.vl4ds4m.board.game.assistant.ui.play

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
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.ui.component.GameSessionCard
import org.vl4ds4m.board.game.assistant.ui.sessionsInfoPreview
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayScreen(
    viewModel: PlayViewModel,
    startNewGame: () -> Unit,
    proceedGame: (String, GameType) -> Unit,
    observeGame: (RemoteSessionInfo) -> Unit,
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
    clickRemoteGame: (RemoteSessionInfo) -> Unit,
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
                        name = "${index + 1}. ${session.name}",
                        type = stringResource(session.type.localizedStringId),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        clickSession(session.id, session.type)
                    }
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
                        name = "${index + 1}. ${session.name}",
                        type = stringResource(session.type.localizedStringId),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        session.run { clickRemoteGame(session) }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PlayScreenWithSessionsPreview() {
    val sessionsInfo = sessionsInfoPreview.map {
        it.copy(completed = false)
    }
    val remoteSessions = listOf(
        RemoteSessionInfo(
            id = "remote_1",
            type = SimpleOrdered,
            name = "Milki Way",
            ip = "100.0.0.100",
            port = 11234
        ),
        RemoteSessionInfo(
            id = "remote_2",
            type = Free,
            name = "Catch me if you can",
            ip = "100.0.0.105",
            port = 41831
        )
    )
    PlayScreenPreview(sessionsInfo, remoteSessions)
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
            clickRemoteGame = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
