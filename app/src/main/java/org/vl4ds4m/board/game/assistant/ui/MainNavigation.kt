package org.vl4ds4m.board.game.assistant.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.ui.game.Game
import org.vl4ds4m.board.game.assistant.ui.game.NewGameStart
import org.vl4ds4m.board.game.assistant.ui.game.gameNavigation
import org.vl4ds4m.board.game.assistant.ui.home.HomeScreen
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileScreen
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
    composable<Profile> {
        ProfileScreen()
    }
    gameNavigation(navController)
    resultsNavigation(navController)
}
