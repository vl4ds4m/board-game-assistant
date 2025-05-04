package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.vl4ds4m.board.game.assistant.States
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.currentPlayerChangedAction
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.playerStateChangedAction
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistory
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistoryManager
import org.vl4ds4m.board.game.assistant.ui.game.component.GameHistoryState
import org.vl4ds4m.board.game.assistant.ui.game.component.GameMenu
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.game.component.Timer
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
    val timer = produceState<Int?>(null, viewModel) {
        viewModel.timeout.combine(viewModel.secondsToEnd) { timeout, seconds ->
            value = seconds.takeIf { timeout }
        }.launchIn(this)
    }
    val ui = viewModel.gameUi
    GameScreenContent(
        players = viewModel.players.collectAsState(),
        currentPlayerId = viewModel.currentPlayerId.collectAsState(),
        actions = viewModel.actions.collectAsState(),
        showAction = ui.actionLog,
        selectPlayer = ui.onPlayerSelected,
        timer = timer,
        playerStats = ui.playerStats,
        masterActions = ui.masterActions,
        modifier = modifier
    )
}

@Composable
fun GameScreenContent(
    players: State<Players>,
    currentPlayerId: State<Long?>,
    actions: State<Actions>,
    showAction: @Composable (GameAction, Players) -> String,
    selectPlayer: ((Long) -> Unit)?,
    timer: State<Int?>,
    playerStats: @Composable RowScope.(State<PlayerState>) -> Unit,
    masterActions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val timerEnabled = remember {
            derivedStateOf { timer.value != null }
        }
        if (timerEnabled.value) {
            Timer(
                state = timer,
                modifier = Modifier.padding(start = 32.dp)
            )
        }
        HorizontalDivider()
        val activePlayers = remember {
            derivedStateOf {
                players.value.filterValues { it.active }
            }
        }
        PlayersRating(
            players = activePlayers,
            currentPlayerId = currentPlayerId,
            onSelectPlayer = selectPlayer,
            playerStats = playerStats,
            modifier = Modifier
                .weight(3f)
                .padding(horizontal = 16.dp)
        )
        HorizontalDivider()
        GameHistory(
            players = players,
            actions = actions,
            showAction = showAction,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        HorizontalDivider()
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            masterActions()
        }
        Spacer(Modifier.size(4.dp))
    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    BoardGameAssistantTheme {
        GameScreenContent(
            players = rememberUpdatedState(previewPlayers),
            currentPlayerId = rememberUpdatedState(1),
            actions = rememberUpdatedState(previewActions),
            showAction = GameUI.actionLog,
            selectPlayer = null,
            timer = rememberUpdatedState(157),
            playerStats = GameUI.playerStats,
            masterActions = GameUI.masterActionsPreview,
            modifier = Modifier.fillMaxSize()
        )
    }
}

val previewPlayers: Players = sequence {
    yield("Abc" to 123)
    yield("Def" to 74)
    yield("Foo" to 92123)
    yield("Bar" to 986)
    repeat(10) { yield("Copy" to 111) }
}.mapIndexed { i, (name, score) ->
    (i + 1L) to Player(
        netDevId = null,
        name = name,
        active = true,
        state = PlayerState(score, mapOf())
    )
}.toMap()

val previewActions: Actions = sequence {
    repeat(10) {
        playerStateChangedAction(
            id = 1L,
            States(
                prev = PlayerState(123, mapOf()),
                next = PlayerState(678, mapOf())
            )
        ).let { yield(it) }
        currentPlayerChangedAction(
            States(
                prev = 3L,
                next = 2L
            )
        ).let { yield(it) }
    }
}.toList()

