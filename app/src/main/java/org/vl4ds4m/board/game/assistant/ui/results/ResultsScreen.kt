package org.vl4ds4m.board.game.assistant.ui.results

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.defaultGames
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel,
    clickSession: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    ResultsScreenContent(
        sessions = viewModel.sessions.collectAsState(),
        clickSession = clickSession,
        modifier = modifier
    )
}

@Composable
fun ResultsScreenContent(
    sessions: State<List<GameSessionInfo>>,
    clickSession: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Completed games",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            sessions.value.sortedByDescending { it.stopTime }
                .onEachIndexed { index, session ->
                    item(session.id) {
                        Text(
                            text = "${index + 1}. ${session.name}",
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .clickable { clickSession(session.id) },
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
        }
    }
}

@Preview
@Composable
private fun ResultsScreenPreview() {
    BoardGameAssistantTheme {
        ResultsScreenContent(
            sessions = remember { mutableStateOf(sessionsInfo) },
            clickSession = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

private val sessionsInfo: List<GameSessionInfo> = defaultGames.filter { it.completed }
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
