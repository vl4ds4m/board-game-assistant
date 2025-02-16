package org.vl4ds4m.board.game.assistant.ui.game

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.ui.Home
import org.vl4ds4m.board.game.assistant.ui.game.end.EndGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.start.NewGamePlayersScreen
import org.vl4ds4m.board.game.assistant.ui.game.start.NewGameStartScreen

@Serializable
object NewGameStart

@Serializable
object NewGamePlayers

@Serializable
data class Game(val type: String, val sessionId: Long? = null)

@Serializable
object End

fun NavGraphBuilder.gameNavigation(navController: NavController) {
    composable<NewGameStart> {
        NewGameStartScreen(
            viewModel = viewModel(),
            onSetupPlayers = {
                navController.navigate(NewGamePlayers)
            }
        )
    }
    composable<NewGamePlayers> { entry ->
        val backStackEntry = navController.rememberTopmost<NewGameStart>(entry)
        NewGamePlayersScreen(
            viewModel = viewModel(
                viewModelStoreOwner = backStackEntry
            ),
            onStartGame = { type ->
                val game = Game(type.title)
                navController.navigate(game) {
                    popUpTo<Home>()
                }
            }
        )
    }
    composable<Game> {
        GameScreen(
            game = it.toRoute<Game>(),
            onGameComplete = {
                navController.navigate(End)
            }
        )
    }
    composable<End> {
        EndGameScreen(
            onHomeNavigate = {
                navController.navigate(Home) {
                    popUpTo<Home>()
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
private inline fun <reified T : Any> NavController.rememberTopmost(
    key: NavBackStackEntry
): NavBackStackEntry = remember(key) {
    Log.d("GameNavigation", "Remember ${T::class.simpleName} navBackStackEntry")
    getBackStackEntry<T>()
}
