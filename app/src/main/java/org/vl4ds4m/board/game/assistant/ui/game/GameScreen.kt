package org.vl4ds4m.board.game.assistant.ui.game

import androidx.activity.addCallback
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.Actions
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.Users
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.ui.MainActivity
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState
import org.vl4ds4m.board.game.assistant.ui.detailedGameSessionPreview
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
    val stopDialogOpened = remember { mutableStateOf(false) }
    val openStopDialog = { stopDialogOpened.value = true }
    LocalActivity.current.let {
        it as MainActivity
    }.onBackPressedDispatcher.addCallback(
        owner = LocalLifecycleOwner.current
    ) {
        openStopDialog()
    }
    StopGameDialog(
        opened = stopDialogOpened,
        onConfirm = navActions.navigateBack
    )
    topBarUiState.update(
        title = viewModel.name.collectAsState().value,
        navigateBack = openStopDialog
    ) {
        GameHistoryManager(
            GameHistoryState(
                reverted = viewModel.reverted.collectAsState(),
                repeatable = viewModel.repeatable.collectAsState(),
                revert = viewModel::revert,
                repeat = viewModel::repeat
            )
        )
        GameMenu(navActions, viewModel::complete)
    }
    LifecycleStartEffect(viewModel) {
        viewModel.start()
        onStopOrDispose {
            viewModel.stop()
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.completed.collect { completed ->
            if (completed) {
                navActions.completeGame()
            }
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
        users = viewModel.users.collectAsState(),
        currentPid = viewModel.currentPid.collectAsState(),
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
    users: State<Users>,
    currentPid: State<PID?>,
    actions: State<Actions>,
    showAction: @Composable (GameAction, Players) -> String,
    selectPlayer: ((PID) -> Unit)?,
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
        PlayersRating(
            players = remember {
                derivedStateOf {
                    players.value.filterValues { it.active }
                }
            },
            users = users,
            currentPid = currentPid,
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

@Composable
private fun StopGameDialog(
    opened: MutableState<Boolean>,
    onConfirm: () -> Unit
) {
    if (opened.value) {
        AlertDialog(
            onDismissRequest = { opened.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        opened.value = false
                        onConfirm()
                    }
                ) {
                    Text(stringResource(R.string.game_action_exit_confirm))
                }
            },
            text = {
                Text(stringResource(R.string.game_action_exit_text))
            }
        )
    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    BoardGameAssistantTheme {
        with(detailedGameSessionPreview) {
            GameScreenContent(
                players = rememberUpdatedState(players.toMap()),
                users = rememberUpdatedState(users),
                currentPid = rememberUpdatedState(currentPid),
                actions = rememberUpdatedState(actions),
                showAction = GameUI.actionLog,
                selectPlayer = null,
                timer = rememberUpdatedState(secondsUntilEnd),
                playerStats = GameUI.playerStats,
                masterActions = GameUI.masterActionsPreview,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
