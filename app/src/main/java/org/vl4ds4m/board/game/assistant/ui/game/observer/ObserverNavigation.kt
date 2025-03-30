package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.ui.Home
import org.vl4ds4m.board.game.assistant.ui.rememberTopmost

@Serializable
data class ObserverStartup(val id: Int)

@Serializable
data object ObserverInGame

fun NavGraphBuilder.observerNavigation(navController: NavController) {
    composable<ObserverStartup> {
        ObserverStartupScreen(
            viewModel = viewModel(),
            onBackClick = { navController.navigateUp() },
            //enterGame = { navController.navigate(ObserverInGame) }
        )
    }
    composable<ObserverInGame> { entry ->
        ObserverGameScreen(
            viewModel = viewModel(
                navController.rememberTopmost<ObserverStartup>(entry)
            ),
            onBackClick = {
                navController.navigate(Home) {
                    popUpTo<Home>()
                    launchSingleTop = true
                }
            }
        )
    }
}
