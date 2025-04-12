package org.vl4ds4m.board.game.assistant.ui

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.ui.game.Game
import org.vl4ds4m.board.game.assistant.ui.game.NewGameStart
import org.vl4ds4m.board.game.assistant.ui.home.HomeScreen
import org.vl4ds4m.board.game.assistant.ui.home.HomeViewModel
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileScreen
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileViewModel
import org.vl4ds4m.board.game.assistant.ui.results.resultsNavigation

sealed interface TopRoute

@Serializable
data object Home : TopRoute

@Serializable
data object Results : TopRoute

@Serializable
data object Profile : TopRoute

fun NavGraphBuilder.topNavigation(navController: NavController) {
    composable<Home> {
        HomeScreen(
            viewModel = viewModel(factory = HomeViewModel.Factory),
            startNewGame = { navController.navigate(NewGameStart) },
            proceedGame = { id, type ->
                Game(type = type.title, sessionId = id).let {
                    navController.navigate(it)
                }
            },
            observeGame = { id, title, ip, port ->
                RemoteSessionInfo(
                    id = id, name = title,
                    ip = ip, port = port
                ).let {
                    navController.navigate(it)
                }
            }
        )
    }
    composable<Profile> {
        ProfileScreen(viewModel = viewModel(factory = ProfileViewModel.Factory))
    }
    resultsNavigation(navController)
}
