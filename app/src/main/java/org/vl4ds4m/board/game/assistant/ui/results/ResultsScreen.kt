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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun ResultsScreen(
    sessions: Map<Long, GameSession>,
    onSessionClick: (Long) -> Unit,
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
            sessions.filterValues { it.completed }
                .onEachIndexed { index, (id, session) ->
                    item(id) {
                        Text(
                            text = "${index + 1}. ${session.name}",
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .clickable { onSessionClick(id) },
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
        ResultsScreen(
            sessions = Store.sessions,
            onSessionClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
