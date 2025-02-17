package org.vl4ds4m.board.game.assistant.ui.game

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.domain.game.Free
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGameType
import org.vl4ds4m.board.game.assistant.domain.Player
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun GameScreen(
    game: Game,
    onGameComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val type = GameType.valueOf(game.type)
    val viewModelFactory = GameViewModelFactory.create(
        type = type,
        sessionId = game.sessionId
    )
    when (type) {
        is Free -> {
            FreeGameScreen(
                viewModel = viewModel(
                    factory = viewModelFactory
                ),
                onGameComplete = onGameComplete,
                modifier = modifier
            )
        }
        is OrderedGameType -> {
            OrderedGameScreen(
                type = type,
                viewModelFactory = viewModelFactory,
                onGameComplete = onGameComplete,
                modifier = modifier
            )
        }
    }
}

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameComplete: () -> Unit,
    currentPlayerId: State<Long?>,
    onSelectPlayer: ((Long) -> Unit)?,
    masterActions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    GameScreenContent(
        name = viewModel.name,
        players = viewModel.players.collectAsState(),
        currentPlayerId = currentPlayerId,
        onSelectPlayer = onSelectPlayer,
        masterActions = masterActions,
        onGameComplete = onGameComplete,
        modifier = modifier
    )
}

@Composable
fun GameScreenContent(
    name: String,
    players: State<Map<Long, Player>>,
    currentPlayerId: State<Long?>,
    onSelectPlayer: ((Long) -> Unit)?,
    masterActions: @Composable () -> Unit,
    onGameComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.clickable { onGameComplete() }
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

@Preview
@Composable
private fun SimpleGameScreenPreview() {
    GameScreenPreview(
        name = "Simple game",
        masterActions = {
            ScoreCounter(
                onPointsAdd = {}
            )
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
internal fun GameScreenPreview(
    name: String,
    masterActions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    BoardGameAssistantTheme {
        GameScreenContent(
            name = name,
            players = mutableStateOf(fakePlayers),
            currentPlayerId = mutableStateOf(null),
            onSelectPlayer = null,
            masterActions = masterActions,
            onGameComplete = {},
            modifier = modifier
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
    (i + 1L) to Player(
        name = name,
        active = true,
        score = score
    )
}.toMap()
