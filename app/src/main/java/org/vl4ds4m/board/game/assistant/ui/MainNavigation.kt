package org.vl4ds4m.board.game.assistant.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.ui.game.NewGameStart
import org.vl4ds4m.board.game.assistant.ui.game.gameNavigation
import org.vl4ds4m.board.game.assistant.ui.home.HomeScreen
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileScreen
import org.vl4ds4m.board.game.assistant.ui.results.ResultsScreen

@Serializable object Home
@Serializable object Profile
@Serializable object Results

fun NavGraphBuilder.mainNavigation(navController: NavController) {
    // top level screens
    composable<Home> {
        HomeScreen(
            onStartNewGame = { navController.navigate(NewGameStart) }
        )
    }
    composable<Results> {
        ResultsScreen()
    }
    composable<Profile> {
        ProfileScreen()
    }

    // game screens
    gameNavigation(navController)
}
