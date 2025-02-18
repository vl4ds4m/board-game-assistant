package org.vl4ds4m.board.game.assistant.ui.game

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.domain.Player
import org.vl4ds4m.board.game.assistant.domain.game.Free
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGameType
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModelFactory
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun GameScreen(
    game: Game,
    gameModifier: GameModifier,
    modifier: Modifier = Modifier
) {
    val type = GameType.valueOf(game.type)
    val viewModelFactory = GameViewModelFactory.create(
        type = type,
        sessionId = game.sessionId
    )
    val viewModel = viewModel<GameViewModel>(
        factory = viewModelFactory
    )
    when (type) {
        is Free -> {
            FreeGameScreen(
                viewModel = viewModel as FreeGameViewModel,
                gameModifier = gameModifier,
                modifier = modifier
            )
        }
        is OrderedGameType -> {
            OrderedGameScreen(
                type = type,
                viewModel = viewModel,
                gameModifier = gameModifier,
                modifier = modifier
            )
        }
    }
}

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNameFormat: (String) -> String,
    currentPlayerId: State<Long?>,
    onSelectPlayer: ((Long) -> Unit)?,
    masterActions: @Composable () -> Unit,
    gameModifier: GameModifier,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.name.collect {
            gameModifier.topAppBarText?.value = onNameFormat(it)
        }
    }
    GameScreenContent(
        players = viewModel.players.collectAsState(),
        currentPlayerId = currentPlayerId,
        onSelectPlayer = onSelectPlayer,
        masterActions = masterActions,
        modifier = modifier
    )
}

@Composable
fun GameScreenContent(
    players: State<Map<Long, Player>>,
    currentPlayerId: State<Long?>,
    onSelectPlayer: ((Long) -> Unit)?,
    masterActions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
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
    masterActions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    BoardGameAssistantTheme {
        GameScreenContent(
            players = mutableStateOf(fakePlayers),
            currentPlayerId = mutableStateOf(1),
            onSelectPlayer = null,
            masterActions = masterActions,
            modifier = modifier
        )
    }
}

private val fakePlayers = sequence {
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
