package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.OrderedGameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.data.Score
import org.vl4ds4m.board.game.assistant.game.log.CurrentPlayerChangeAction
import org.vl4ds4m.board.game.assistant.game.log.PlayerStateChangeAction
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistory
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.component.GameTopBar
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun GameScreen(
    topBarTitle: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    navActions: GameNavActions? = null,
    history: GameHistory? = null,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            GameTopBar(
                title = topBarTitle,
                onArrowBackClick = onBackClick,
                navActions = navActions,
                history = history
            )
        }
    ) { innerPadding ->
        val m = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        content(m)
    }
}

@Composable
fun GameScreen(
    type: GameType,
    sessionId: Long?,
    navActions: GameNavActions,
    modifier: Modifier = Modifier
) {
    when (type) {
        is Free -> {
            FreeGameScreen(
                sessionId = sessionId,
                navActions = navActions,
                modifier = modifier
            )
        }
        is OrderedGameType -> {
            OrderedGameScreen(
                type = type,
                sessionId = sessionId,
                navActions = navActions,
                modifier = modifier
            )
        }
    }
}

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNameFormat: (String) -> String,
    currentPlayerId: StateFlow<Long?>,
    onSelectPlayer: ((Long) -> Unit)?,
    masterActions: @Composable () -> Unit,
    navActions: GameNavActions,
    modifier: Modifier = Modifier
) {
    DisposableEffect(Unit) {
        viewModel.start()
        onDispose {
            viewModel.stop()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.completed.collect { completed ->
            if (completed) navActions.onGameComplete()
        }
    }
    val name = viewModel.name.collectAsState()
    GameScreen(
        topBarTitle = onNameFormat(name.value),
        onBackClick = navActions.onBackClick,
        modifier = modifier,
        navActions = navActions.copy(
            onGameComplete = viewModel::complete
        ),
        history = GameHistory(
            reverted = viewModel.reverted.collectAsState(),
            repeatable = viewModel.repeatable.collectAsState(),
            onRevertAction = viewModel::revert,
            onRepeatAction = viewModel::repeat
        )
    ) { innerModifier ->
        GameScreenContent(
            players = viewModel.players.collectAsState(),
            currentPlayerId = currentPlayerId.collectAsState(),
            actions = viewModel.actions.collectAsState(),
            onSelectPlayer = onSelectPlayer,
            masterActions = masterActions,
            modifier = innerModifier
        )
    }
}

@Composable
fun GameScreenContent(
    players: State<Players>,
    currentPlayerId: State<Long?>,
    actions: State<Actions>,
    onSelectPlayer: ((Long) -> Unit)?,
    masterActions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val activePlayers = remember {
            derivedStateOf {
                players.value.filterValues { it.active }
            }
        }
        PlayersRating(
            players = activePlayers,
            currentPlayerId = currentPlayerId,
            onSelectPlayer = onSelectPlayer,
            modifier = Modifier.weight(1f)
        )
        GameHistory(
            players = players,
            actions = actions,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            masterActions()
        }
    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    BoardGameAssistantTheme {
        GameScreen(
            topBarTitle = "Some game",
            onBackClick = {},
            modifier = Modifier.fillMaxSize(),
            navActions = GameNavActions.Empty,
            history = GameHistory.Empty
        ) {
            GameScreenContent(
                players = remember { mutableStateOf(fakePlayers) },
                currentPlayerId = remember { mutableStateOf(1) },
                actions = remember { mutableStateOf(fakeActions) },
                onSelectPlayer = null,
                masterActions = { ScoreCounter({}) },
                modifier = it
            )
        }
    }
}

val fakePlayers = sequence {
    yield("Abc" to 123)
    yield("Def" to 456)
    yield("Foo" to 43)
    yield("Bar" to 4)
    repeat(10) { yield("Copy" to 111) }
}.mapIndexed { i, (name, score) ->
    (i + 1L) to Player(
        name = name,
        active = true,
        state = Score(score)
    )
}.toMap()

val fakeActions = listOf(
    CurrentPlayerChangeAction(3, 4),
    PlayerStateChangeAction(4, Score(2), Score(7))
)
