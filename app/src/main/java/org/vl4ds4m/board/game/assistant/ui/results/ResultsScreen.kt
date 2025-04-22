package org.vl4ds4m.board.game.assistant.ui.results

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.ui.component.GameSessionCard
import org.vl4ds4m.board.game.assistant.ui.play.gameSessionsPreview
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel,
    clickSession: (String) -> Unit,
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
    clickSession: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.results_title),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineMedium
        )
        HorizontalDivider()
        if (sessions.value.isEmpty()) {
            Text(
                text = stringResource(R.string.results_empty_list),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(
                    items = sessions.value.sortedByDescending { it.stopTime },
                    key = { _, s -> s.id }
                ) { index, session ->
                    GameSessionCard(
                        text = "${index + 1}. ${session.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { clickSession(session.id) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SomeResultsScreenPreview() {
    ResultsScreenPreview(sessionsInfo)
}

@Preview
@Composable
private fun EmptyResultsScreenPreview() {
    ResultsScreenPreview(listOf())
}

@Composable
private fun ResultsScreenPreview(sessionsInfo: List<GameSessionInfo>) {
    BoardGameAssistantTheme {
        ResultsScreenContent(
            sessions = remember { mutableStateOf(sessionsInfo) },
            clickSession = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

private val sessionsInfo: List<GameSessionInfo> get() =
    gameSessionsPreview.mapIndexed { i, s ->
        GameSessionInfo(
            id = i.inc().toString(),
            completed = true,
            type = s.type,
            name = s.name,
            startTime = s.startTime,
            stopTime = s.stopTime
        )
    }
