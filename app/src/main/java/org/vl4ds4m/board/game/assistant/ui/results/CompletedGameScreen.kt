package org.vl4ds4m.board.game.assistant.ui.results

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.localTime
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState
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
        topBarUiState.update(
            title = "Game results",
            navigateBack = navigateBack
        )
        value = viewModel.getSession(sessionId).also {
            it?.also {
                topBarUiState.update(
                    title = "'${it.name}' results",
                    navigateBack = navigateBack
                )
            } ?: Log.e(
                "GameResults",
                "Can't load completed session[id = $sessionId]"
            )
        }
    }.value?.also {
        CompletedGameScreenContent(
            type = it.type,
            players = it.players,
            startTime = it.startTime,
            stopTime = it.stopTime,
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
    players: Players,
    startTime: Long?,
    stopTime: Long?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Param(
            name  = "Type",
            value = type.title
        )
        Param(
            name = "Player count",
            value = players.size.toString()
        )
        Param(
            name = "Start time",
            value = startTime?.localTime?.formatted ?: "no data"
        )
        Param(
            name = "Stop time",
            value = stopTime?.localTime?.formatted ?: "no data"
        )
        HorizontalDivider()
        Text(
            text = "Players rating",
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        PlayersRating(
            players = rememberUpdatedState(players),
            currentPlayerId = remember { mutableStateOf(null) },
            onSelectPlayer = null,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
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
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

private val LocalDateTime.formatted: String get() =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        .format(this)

@Preview
@Composable
private fun CompletedGameScreenPreview() {
    BoardGameAssistantTheme {
        CompletedGameScreenContent(
            type = Free,
            players = mapOf(
                1L to Player(
                    netDevId = null,
                    name = "Abv",
                    active = true,
                    state = PlayerState(45, mapOf())
                ),
                2L to Player(
                    netDevId = null,
                    name = "Efo",
                    active = false,
                    state = PlayerState(123, mapOf())
                ),
                3L to Player(
                    netDevId = null,
                    name = "Urt",
                    active = true,
                    state = PlayerState(59, mapOf())
                )
            ),
            startTime = System.currentTimeMillis() - 11_000,
            stopTime = System.currentTimeMillis(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
