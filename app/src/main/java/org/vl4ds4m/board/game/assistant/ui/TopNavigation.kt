package org.vl4ds4m.board.game.assistant.ui

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState
import org.vl4ds4m.board.game.assistant.ui.game.Game
import org.vl4ds4m.board.game.assistant.ui.game.NewGameStart
import org.vl4ds4m.board.game.assistant.ui.game.observer.GameObserver
import org.vl4ds4m.board.game.assistant.ui.play.PlayScreen
import org.vl4ds4m.board.game.assistant.ui.play.PlayViewModel
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileScreen
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileViewModel
import org.vl4ds4m.board.game.assistant.ui.results.resultsNavigation

sealed interface TopRoute

@Serializable
data object Play : TopRoute

@Serializable
data object Results : TopRoute

@Serializable
data object Profile : TopRoute

fun NavGraphBuilder.topNavigation(
    navController: NavController,
    topBarUiState: TopBarUiState
) {
    composable<Play> {
        PlayScreen(
            viewModel = viewModel(factory = PlayViewModel.Factory),
            startNewGame = { navController.navigate(NewGameStart) },
            proceedGame = { id, type ->
                Game(type = type.title, sessionId = id).let {
                    navController.navigate(it)
                }
            },
            observeGame = { info ->
                val route = GameObserver.from(info)
                navController.navigate(route)
            }
        )
    }
    composable<Profile> {
        ProfileScreen(viewModel = viewModel(factory = ProfileViewModel.Factory))
    }
    resultsNavigation(navController, topBarUiState)
}
