package org.vl4ds4m.board.game.assistant.ui.results

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.OrderedPlayers
import org.vl4ds4m.board.game.assistant.game.Users
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.localDateTime
import org.vl4ds4m.board.game.assistant.prettyTime
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState
import org.vl4ds4m.board.game.assistant.ui.detailedGameSessionPreview
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistory
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CompletedGameScreen(
    topBarUiState: TopBarUiState,
    viewModel: ResultsViewModel,
    sessionId: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    produceState<GameSession?>(null) {
        topBarUiState.update(navigateBack = navigateBack)
        value = viewModel.getSession(sessionId).also {
            it ?: Log.e(
                "GameResults",
                "Can't load completed session[id = $sessionId]"
            )
        }
    }.value?.also {
        topBarUiState.update(
            title = stringResource(R.string.game_results_title_prefix)
                + ": " + it.name,
            navigateBack = navigateBack
        ) {
            RemoveButton {
                navigateBack()
                viewModel.removeSession(sessionId)
            }
        }
        CompletedGameScreenContent(
            type = it.type,
            players = it.players,
            users = it.users,
            startTime = it.startTime,
            stopTime = it.stopTime,
            duration = it.duration,
            actions = it.actions,
            modifier = modifier
        )
    } ?: run {
        Text(
            text = "Wait for loading ...",
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}

@Composable
fun CompletedGameScreenContent(
    type: GameType,
    players: OrderedPlayers,
    users: Users,
    startTime: Long?,
    stopTime: Long?,
    duration: Long?,
    actions: Actions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Param(
            name  = stringResource(R.string.game_results_type),
            value = stringResource(type.nameResId)
        )
        Param(
            name = stringResource(R.string.game_results_player_count),
            value = players.size.toString()
        )
        Param(
            name = stringResource(R.string.game_results_start_time),
            value = startTime?.localDateTime?.formatted ?: "no data"
        )
        Param(
            name = stringResource(R.string.game_results_stop_time),
            value = stopTime?.localDateTime?.formatted ?: "no data"
        )
        Param(
            name = stringResource(R.string.game_results_duration),
            value = duration?.let {
                prettyTime((it / 1000).toInt())
            } ?: "no data"
        )
        HorizontalDivider()
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            val ratingVisible = rememberSaveable { mutableStateOf(true) }
            Row(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                StatsChip(
                    text = stringResource(R.string.game_results_rating),
                    selected = ratingVisible.value
                ) {
                    ratingVisible.value = true
                }
                Spacer(Modifier.weight(1f))
                StatsChip(
                    text = stringResource(R.string.game_results_actions),
                    selected = !ratingVisible.value
                ) {
                    ratingVisible.value = false
                }
            }
            val ps = remember { mutableStateOf(players.toMap()) }
            if (ratingVisible.value) {
                PlayersRating(
                    players = ps,
                    users = remember { mutableStateOf(users) },
                    currentPid = remember { mutableStateOf(null) },
                    onSelectPlayer = null,
                    playerStats = type.uiFactory.playerStats,
                    modifier = Modifier.weight(1f)
                )
            } else {
                GameHistory(
                    players = ps,
                    actions = remember { mutableStateOf(actions) },
                    showAction = type.uiFactory.actionLog,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RemoveButton(onRemove: () -> Unit) {
    val removeDialogOpened = remember { mutableStateOf(false) }
    IconButton(
        onClick = { removeDialogOpened.value = true }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Remove"
        )
    }
    val close = { removeDialogOpened.value = false }
    if (removeDialogOpened.value) {
        AlertDialog(
            onDismissRequest = close,
            confirmButton = {
                Button(
                    onClick = {
                        close()
                        onRemove()
                    }
                ) {
                    Text(stringResource(R.string.game_results_remove_confirm))
                }
            },
            dismissButton = {
                Button(close) {
                    Text(stringResource(R.string.game_results_remove_cancel))
                }
            },
            text = {
                Text(stringResource(R.string.game_results_remove_msg))
            }
        )
    }
}

@Composable
private fun Param(name: String, value: String) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "$name: ",
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1
        )
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1
        )
    }
}

@Composable
private fun StatsChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
        },
        modifier = Modifier.height(40.dp)
    )
}

private val LocalDateTime.formatted: String get() =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        .format(this)

@Preview
@Composable
private fun CompletedGameScreenPreview() {
    BoardGameAssistantTheme {
        with(detailedGameSessionPreview) {
            CompletedGameScreenContent(
                type = type,
                players = players,
                users = users,
                startTime = startTime,
                stopTime = stopTime,
                duration = duration,
                actions = actions,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
