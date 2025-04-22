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
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
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
    PlayScreenPreview(sessionsInfoPreview, remoteSessionPreview)
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

private val initialTime: Long = java.time.Instant
    .parse("2025-01-24T10:15:34.00Z").epochSecond


val gameSessionsPreview: List<GameSession> get() = listOf(
    GameSession(
        completed = false,
        type = SimpleOrdered,
        name = "Uno 93",
        players = listOf(
            1L to Player(
                netDevId = null,
                name = "Abc",
                active = true,
                state = PlayerState(120, mapOf())
            ),
            2L to Player(
                netDevId = null,
                name = "Def",
                active = false,
                state = PlayerState(36, mapOf())
            ),
            3L to Player(
                netDevId = null,
                name = "Foo",
                active = true,
                state = PlayerState(154, mapOf())
            )
        ),
        currentPlayerId = 1L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 40_000,
        stopTime = initialTime + 40_005,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0
    ),
    GameSession(
        completed = false,
        type = Free,
        name = "Poker Counts 28",
        players = listOf(
            1L to Player(
                netDevId = null,
                name = "Bar",
                active = true,
                state = PlayerState(1220, mapOf())
            ),
            2L to Player(
                netDevId = null,
                name = "Conf",
                active = true,
                state = PlayerState(376, mapOf())
            ),
            3L to Player(
                netDevId = null,
                name = "Leak",
                active = true,
                state = PlayerState(532, mapOf())
            )
        ),
        currentPlayerId = 2L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 20_000,
        stopTime = initialTime + 20_015,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0
    ),
    GameSession(
        type = SimpleOrdered,
        completed = true,
        name = "Imaginarium 74",
        players = listOf(
            1L to Player(
                netDevId = null,
                name = "Bar",
                active = true,
                state = PlayerState(12, mapOf())
            ),
            2L to Player(
                netDevId = null,
                name = "Conf",
                active = true,
                state = PlayerState(37, mapOf())
            ),
            3L to Player(
                netDevId = null,
                name = "Leak",
                active = true,
                state = PlayerState(53, mapOf())
            ),
            4L to Player(
                netDevId = null,
                name = "Flick",
                active = true,
                state = PlayerState(32, mapOf())
            )
        ),
        currentPlayerId = 3L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 30_000,
        stopTime = initialTime + 30_010,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0
    )
)

private val remoteSessionPreview get() = listOf(
    RemoteSessionInfo("remote_1", "Milki Way", "100.0.0.100", 11234),
    RemoteSessionInfo("remote_2", "Catch me if you can", "100.0.0.100", 11234)
)

private val sessionsInfoPreview: List<GameSessionInfo> get() =
    gameSessionsPreview.filter { !it.completed }
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
