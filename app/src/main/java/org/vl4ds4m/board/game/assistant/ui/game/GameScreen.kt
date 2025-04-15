package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.Carcassonne
import org.vl4ds4m.board.game.assistant.game.Dice
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.data.Score
import org.vl4ds4m.board.game.assistant.game.log.CurrentPlayerChangeAction
import org.vl4ds4m.board.game.assistant.game.log.PlayerStateChangeAction
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistory
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistoryManager
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistoryState
import org.vl4ds4m.board.game.assistant.ui.game.component.GameMenu
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.dice.DiceGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.ordered.SimpleOrderedGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun GameScreen(
    type: GameType,
    sessionId: String?,
    topBarUiState: TopBarUiState,
    navActions: GameNavActions,
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel<GameViewModel>(
        factory = GameViewModel.createFactory(
            sessionId = sessionId,
            producer = type.viewModelProducer
        )
    )
    topBarUiState.update(
        title = viewModel.name.collectAsState().value,
        navigateBack = navActions.navigateBack
    ) {
        GameHistoryManager(
            GameHistoryState(
                reverted = viewModel.reverted.collectAsState(),
                repeatable = viewModel.repeatable.collectAsState(),
                revert = viewModel::revert,
                repeat = viewModel::repeat
            )
        )
        GameMenu(
            navActions.copy(
                completeGame = viewModel::complete
            )
        )
    }
    LifecycleStartEffect(viewModel) {
        viewModel.start()
        onStopOrDispose {
            viewModel.stop()
        }
    }
    LaunchedEffect(viewModel, navActions) {
        viewModel.completed.collect { completed ->
            if (completed) navActions.completeGame()
        }
    }
    when (type) {
        is Free          -> FreeGameScreen(modifier)
        is SimpleOrdered -> SimpleOrderedGameScreen(modifier)
        is Dice          -> DiceGameScreen(modifier)
        is Carcassonne   -> CarcassonneGameScreen(modifier)
        is Monopoly      -> MonopolyGameScreen(modifier)
    }
}

@Composable
fun GameScreen(
    selectPlayer: ((Long) -> Unit)?,
    modifier: Modifier = Modifier,
    masterActions: @Composable () -> Unit
) {
    val viewModel = viewModel<GameViewModel>()
    GameScreenContent(
        players = viewModel.players.collectAsState(),
        currentPlayerId = viewModel.currentPlayerId.collectAsState(),
        actions = viewModel.actions.collectAsState(),
        selectPlayer = selectPlayer,
        masterActions = masterActions,
        modifier = modifier
    )
}

@Composable
fun GameScreenContent(
    players: State<Players>,
    currentPlayerId: State<Long?>,
    actions: State<Actions>,
    selectPlayer: ((Long) -> Unit)?,
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
            onSelectPlayer = selectPlayer,
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
        GameScreenContent(
            players = remember { mutableStateOf(fakePlayers) },
            currentPlayerId = remember { mutableStateOf(1) },
            actions = remember { mutableStateOf(fakeActions) },
            selectPlayer = null,
            masterActions = { ScoreCounter({}) },
            modifier = Modifier.fillMaxSize()
        )
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
        netDevId = null,
        name = name,
        active = true,
        state = Score(score)
    )
}.toMap()

val fakeActions = listOf(
    CurrentPlayerChangeAction(3, 4),
    PlayerStateChangeAction(4, Score(2), Score(7))
)
