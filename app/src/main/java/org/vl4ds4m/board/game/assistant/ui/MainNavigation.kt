package org.vl4ds4m.board.game.assistant.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo
import org.vl4ds4m.board.game.assistant.ui.game.Game
import org.vl4ds4m.board.game.assistant.ui.game.NewGameStart
import org.vl4ds4m.board.game.assistant.ui.game.gameNavigation
import org.vl4ds4m.board.game.assistant.ui.game.observer.observerNavigation
import org.vl4ds4m.board.game.assistant.ui.home.HomeScreen
import org.vl4ds4m.board.game.assistant.ui.home.HomeViewModel
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileScreen
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileViewModel
import org.vl4ds4m.board.game.assistant.ui.results.resultsNavigation

sealed interface MainRoute

@Serializable
data object Home : MainRoute

@Serializable
data object Results : MainRoute

@Serializable
data object Profile : MainRoute

fun NavGraphBuilder.mainNavigation(navController: NavController) {
    composable<Home> {
        HomeScreen(
            viewModel = viewModel(factory = HomeViewModel.Factory),
            startNewGame = { navController.navigate(NewGameStart) },
            proceedGame = { id, type ->
                navController.navigate(Game(type.title, id))
            },
            observeGame = { id, title, ip, port ->
                RemoteSessionInfo(id, title, ip, port).let {
                    navController.navigate(it)
                }
            }
        )
    }
    gameNavigation(navController)
    observerNavigation(navController)
    composable<Profile> {
        ProfileScreen(viewModel = viewModel(factory = ProfileViewModel.Factory))
    }
    resultsNavigation(navController)
}

@Composable
inline fun <reified T : Any> NavController.rememberTopmost(
    key: NavBackStackEntry
): NavBackStackEntry = remember(key) {
    Log.d("Navigation", "Remember ${T::class.simpleName} navBackStackEntry")
    getBackStackEntry<T>()
}
