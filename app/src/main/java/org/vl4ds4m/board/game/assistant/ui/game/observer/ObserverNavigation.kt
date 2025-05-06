package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.network.NetworkGameState
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState

fun NavGraphBuilder.observerNavigation(
    navController: NavController,
    topBarUiState: TopBarUiState
) {
    composable<RemoteSessionInfo> { entry ->
        val route = entry.toRoute<RemoteSessionInfo>()
        val viewModel = viewModel<GameObserverViewModel>(
            factory = GameObserverViewModel.createFactory(route)
        )
        val observer = viewModel.observerState.collectAsState()
        val session = produceState(emptySession) {
            viewModel.sessionState.collect {
                value = it ?: emptySession
            }
        }
        val onBackClick: () -> Unit = { navController.navigateUp() }
        val title = remember {
            derivedStateOf { session.value.name }
        }
        topBarUiState.update(
            title = title.value,
            navigateBack = onBackClick
        )
        val players = remember {
            derivedStateOf { session.value.players.toMap() }
        }
        val gameUiFactory = remember {
            derivedStateOf { session.value.type.uiFactory }
        }
        when (observer.value) {
            NetworkGameState.REGISTRATION -> ObserverStartupScreen()
            NetworkGameState.IN_GAME -> {
                val currentPid = remember {
                    derivedStateOf { session.value.currentPid }
                }
                val actions = remember {
                    derivedStateOf { session.value.actions }
                }
                val timer = remember {
                    derivedStateOf {
                        session.value.secondsUntilEnd.takeIf { session.value.timeout }
                    }
                }
                ObserverGameScreen(
                    players = players,
                    gameUiFactory = gameUiFactory,
                    currentPid = currentPid,
                    actions = actions,
                    timer = timer
                )
            }
            NetworkGameState.END_GAME -> ObserverEndScreen(
                players = players,
                gameUiFactory = gameUiFactory
            )
            NetworkGameState.EXIT -> onBackClick()
        }
    }
}

private val emptySession = GameSession(
    completed = false,
    type = SimpleOrdered,
    name = "Game",
    players = listOf(),
    currentPid = null,
    nextNewPid = 1,
    startTime = null,
    stopTime = null,
    duration = null,
    timeout = false,
    secondsUntilEnd = 0,
    actions = listOf(),
    currentActionPosition = 0
)
