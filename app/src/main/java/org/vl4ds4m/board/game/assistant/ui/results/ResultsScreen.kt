package org.vl4ds4m.board.game.assistant.ui.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.data.GameSessionInfo
import org.vl4ds4m.board.game.assistant.localDateTime
import org.vl4ds4m.board.game.assistant.ui.component.GameSessionCard
import org.vl4ds4m.board.game.assistant.ui.sessionsInfoPreview
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel,
    clickSession: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ResultsScreenContent(
        sessions = viewModel.sessions.collectAsState(),
        clickSession = clickSession,
        typeFilters = viewModel.typeFilters,
        modifier = modifier
    )
}

@Composable
fun ResultsScreenContent(
    sessions: State<List<GameSessionInfo>>,
    clickSession: (String) -> Unit,
    typeFilters: Filters<GameType>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.width(36.dp))
            Text(
                text = stringResource(R.string.results_title),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineMedium
            )
            val filterExpanded = remember { mutableStateOf(false) }
            IconButton(
                onClick = { filterExpanded.value = true },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.filter_alt_24px),
                    contentDescription = "Filter results"
                )
                TypeFiltersMenu(filterExpanded, typeFilters)
            }
        }
        HorizontalDivider()
        val sortedSessions = sessions.value
            .filter { typeFilters.none || it.type in typeFilters }
            .sortedByDescending { it.stopTime }
        if (sortedSessions.isEmpty()) {
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sortedList(sortedSessions, clickSession)
            }
        }
    }
}

@Composable
private fun TypeFiltersMenu(
    expanded: MutableState<Boolean>,
    types: Filters<GameType>
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        val allTypes = types.rules
        DropdownMenuItem(
            text = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.results_filter_all),
                        modifier = Modifier.weight(1f)
                    )
                    Checkbox(
                        checked = types.all,
                        onCheckedChange = null
                    )
                }
            },
            onClick = {
                if (types.all) {
                    allTypes.forEach { types -= it }
                } else {
                    allTypes.forEach { types += it }
                }
            }
        )
        allTypes.forEach { type ->
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(type.nameResId),
                            modifier = Modifier.weight(1f)
                        )
                        Checkbox(
                            checked = type in types,
                            onCheckedChange = null
                        )
                    }
                },
                onClick = {
                    if (type !in types) {
                        types += type
                    } else {
                        types -= type
                    }
                }
            )
        }
    }
}

private fun LazyListScope.sortedList(
    sessions: List<GameSessionInfo>,
    clickSession: (String) -> Unit
) {
    val now = System.currentTimeMillis().localDateTime.toLocalDate()
    val yesterday = now.minusDays(1)
    var dateGroup: LocalDate? = now
    for ((i, session) in sessions.withIndex()) {
        val date = session.stopTime?.localDateTime?.toLocalDate()
        if (date != null) {
            if (i == 0 || dateGroup == null || dateGroup.isAfter(date)) {
                dateGroup = date
                item {
                    val text = when {
                        now.isEqual(date) -> {
                            stringResource(R.string.results_date_today)
                        }
                        yesterday.isEqual(date) -> {
                            stringResource(R.string.results_date_yesterday)
                        }
                        else -> date.formatted
                    }
                    DateLabel(text, i == 0)
                }
            }
        } else {
            if (dateGroup != null) {
                dateGroup = null
                item {
                    DateLabel("Straight after the Big Bang", i == 0)
                }
            }
        }
        item(session.id) {
            SessionItem(session, clickSession)
        }
    }
}

@Composable
private fun DateLabel(text: String, first: Boolean) {
    Text(
        text = text,
        maxLines = 1,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(
            start = 16.dp,
            top = if (first) 0.dp else 16.dp
        )
    )
}

@Composable
private fun SessionItem(session: GameSessionInfo, onClick: (String) -> Unit) {
    GameSessionCard(
        name = session.name,
        type = stringResource(session.type.nameResId),
        modifier = Modifier.fillMaxWidth()
    )  {
        onClick(session.id)
    }
}

private val LocalDate.formatted: String get() =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        .format(this)

@Preview
@Composable
private fun SomeResultsScreenPreview() {
    val sessionsInfo = sessionsInfoPreview.map {
        it.copy(completed = true)
    }
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
            typeFilters = Filters.empty(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
