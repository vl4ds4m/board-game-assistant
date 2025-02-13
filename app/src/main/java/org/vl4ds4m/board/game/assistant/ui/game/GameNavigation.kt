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
    composable<NewGamePlayers> {
        val backStackEntry = rememberNavBackStackEntry<NewGameStart>(navController, it)
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
            onGameComplete = {}
        )
    }
    composable<End> {
    }
}

@Composable
private inline fun <reified T : Any> rememberNavBackStackEntry(
    navController: NavController,
    currentBackStackEntry: NavBackStackEntry
): NavBackStackEntry = remember(currentBackStackEntry) {
    Log.d("GameNavigation", "Calculate ${T::class.simpleName} navBackStackEntry")
    navController.getBackStackEntry<T>()
}
