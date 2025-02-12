package org.vl4ds4m.board.game.assistant.ui.game

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun GameScreenContent(
    name: String,
    players: State<List<Player>>,
    currentPlayerId: State<Long?>,
    onSelectPlayer: ((Player) -> Unit)?,
    masterActions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(24.dp))
        PlayersRating(
            players = players,
            currentPlayerId = currentPlayerId,
            onSelectPlayer = onSelectPlayer,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            masterActions()
        }
    }
}

@Composable
fun PlayersRating(
    players: State<List<Player>>,
    currentPlayerId: State<Long?>,
    onSelectPlayer: ((Player) -> Unit)?,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(
            items = players.value,
            key = { _, player -> player.id }
        ) { i, player ->
            PlayerInGameCard(
                rating = i + 1,
                name = player.name,
                score = player.score,
                selected = player.id == currentPlayerId.value,
                onSelect = onSelectPlayer?.let { f -> { f(player) } }
            )
        }
    }
}

@Composable
fun ScoreCounter(
    onAddPoints: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val (score, onScoreChanged) = rememberSaveable { mutableStateOf("") }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = score,
            onValueChange = onScoreChanged,
            modifier = Modifier.width(150.dp),
            singleLine = true,
            suffix = { Text(" score(s)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(Modifier.width(24.dp))
        Button(
            onClick = {
                score.toIntOrNull()?.let(onAddPoints)
            },
            modifier = Modifier.width(90.dp)
        ) {
            Text("Apply")
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun GameScreenPreview() {
    BoardGameAssistantTheme {
        GameScreenContent(
            name = "Some game",
            players = mutableStateOf(fakePlayers),
            currentPlayerId = mutableStateOf(null),
            onSelectPlayer = null,
            masterActions = {
                ScoreCounter(
                    onAddPoints = {}
                )
            }
        )
    }
}

internal val fakePlayers = sequence {
    yield("Abc" to 123)
    yield("Def" to 456)
    yield("Foo" to 43)
    yield("Bar" to 4)
    repeat(10) { yield("Copy" to 111) }
}.mapIndexed { i, (name, score) ->
    Player(
        id = i.toLong() + 1,
        name = name,
        active = true,
        score = score
    )
}.toList()
