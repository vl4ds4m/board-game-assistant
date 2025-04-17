package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.vl4ds4m.board.game.assistant.network.NetworkGameState
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState
import org.vl4ds4m.board.game.assistant.ui.game.component.gameActionPresenter

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
        val session = viewModel.sessionState.collectAsState()
        val onBackClick: () -> Unit = { navController.navigateUp() }
        val title = remember {
            derivedStateOf {
                session.value?.let { "${it.name} (${it.type.title})" }
                    ?: route.name
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
                    derivedStateOf { session.value?.players?.toMap() ?: mapOf() }
                }
                val currentPlayerId = remember {
                    derivedStateOf { session.value?.currentPlayerId }
                }
                val actions = remember {
                    derivedStateOf {
                        session.value?.let {
                            it.actions.take(it.currentActionPosition)
                        } ?: listOf()
                    }
                }
                val showAction = remember {
                    derivedStateOf {
                        session.value?.type?.gameActionPresenter
                            ?.let { it::showAction }
                            ?: { _, _ -> "" }
                    }
                }
                ObserverGameScreen(
                    players = players,
                    currentPlayerId = currentPlayerId,
                    actions = actions,
                    showAction = showAction.value
                )
            }
            NetworkGameState.END_GAME -> ObserverEndScreen()
            NetworkGameState.EXIT -> onBackClick()
        }
    }
}
