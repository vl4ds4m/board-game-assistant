package org.vl4ds4m.board.game.assistant.ui.game

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun GameScreen(
    viewModel: OrderedGameViewModel,
    modifier: Modifier = Modifier,
) {
    GameScreenContent(
        name = viewModel.name,
        playersState = viewModel.players.collectAsState(),
        playerScoresState = viewModel.playerScores.collectAsState(),
        onAddScore = { viewModel.addScore(it) },
        modifier = modifier
    )
}

@Composable
fun GameScreenContent(
    name: String,
    playersState: State<List<Player>>,
    playerScoresState: State<Map<Long, Score>>,
    onAddScore: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val (score, onScoreChanged) = rememberSaveable { mutableStateOf("") }
    val players by playersState
    val playerScores by playerScoresState
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(24.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(players) { i, player ->
                PlayerInGameCard(
                    rating = i + 1,
                    name = player.name,
                    score = playerScores[player.id]!!.value
                )
            }
        }
        Spacer(Modifier.height(40.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = score,
                onValueChange = onScoreChanged,
                modifier = Modifier.weight(1f),
                singleLine = true,
                suffix = { Text(" score(s)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(Modifier.width(24.dp))
            Button(
                onClick = {
                    val changing = score.toInt()
                    onAddScore(changing)
                }
            ) {
                Text("Apply")
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun GameScreenPreview() {
    BoardGameAssistantTheme {
        GameScreenContent(
            name = "Simple game",
            playersState = mutableStateOf(listOf()),
            playerScoresState = mutableStateOf(mapOf()),
            onAddScore = {}
        )
    }
}
