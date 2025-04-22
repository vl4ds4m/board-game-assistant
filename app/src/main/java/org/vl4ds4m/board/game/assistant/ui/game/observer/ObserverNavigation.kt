package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.gameActionPresenter
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
        val typeName = stringResource(session.value.type.localizedStringId)
        val title = remember {
            derivedStateOf {
                session.value.let { "${it.name} ($typeName)" }
            }
        }
        topBarUiState.update(
            title = title.value,
            navigateBack = onBackClick
        )
        when (observer.value) {
            NetworkGameState.REGISTRATION -> ObserverStartupScreen()
            NetworkGameState.IN_GAME -> {
                val players = remember {
                    derivedStateOf { session.value.players.toMap() }
                }
                val currentPlayerId = remember {
                    derivedStateOf { session.value.currentPlayerId }
                }
                val actions = remember {
                    derivedStateOf {
                        val presenter = session.value.type.gameActionPresenter
                        session.value.actions.map {
                            presenter.showAction(it, players.value)
                        }
                    }
                }
                val timer = remember {
                    derivedStateOf {
                        session.value.secondsUntilEnd.takeIf { session.value.timeout }
                    }
                }
                ObserverGameScreen(
                    players = players,
                    currentPlayerId = currentPlayerId,
                    actions = actions,
                    timer = timer
                )
            }
            NetworkGameState.END_GAME -> ObserverEndScreen()
            NetworkGameState.EXIT -> onBackClick()
        }
    }
}

private val emptySession = GameSession(
    completed = false,
    type = SimpleOrdered,
    name = "Default",
    players = listOf(),
    currentPlayerId = null,
    nextNewPlayerId = 1L,
    startTime = null,
    stopTime = null,
    duration = null,
    timeout = false,
    secondsUntilEnd = 0,
    actions = listOf(),
    currentActionPosition = 0
)
