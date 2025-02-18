package org.vl4ds4m.board.game.assistant.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.ui.game.Game
import org.vl4ds4m.board.game.assistant.ui.game.NewGameStart
import org.vl4ds4m.board.game.assistant.ui.home.Home
import org.vl4ds4m.board.game.assistant.ui.home.HomeScreen
import org.vl4ds4m.board.game.assistant.ui.profile.Profile
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileScreen
import org.vl4ds4m.board.game.assistant.ui.results.Results
import org.vl4ds4m.board.game.assistant.ui.results.ResultsScreen

fun NavGraphBuilder.mainNavigation(navController: NavController) {
    composable<Home> {
        HomeScreen(
            sessions = Store.sessions,
            onStartNewGame = { navController.navigate(NewGameStart) },
            onSessionClick = { sessionId ->
                Store.load(sessionId)?.type?.let { type ->
                    val game = Game(type.title, sessionId)
                    navController.navigate(game)
                }
            }
        )
    }
    composable<Results> {
        ResultsScreen()
    }
    composable<Profile> {
        ProfileScreen()
    }
}
