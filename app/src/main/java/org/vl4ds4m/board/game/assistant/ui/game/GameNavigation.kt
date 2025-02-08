package org.vl4ds4m.board.game.assistant.ui.game

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.ui.Home
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.free.FreeGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.start.NewGamePlayersScreen
import org.vl4ds4m.board.game.assistant.ui.game.start.NewGameStartScreen

@Serializable
object NewGameStart
@Serializable
object NewGamePlayers
@Serializable
object Game

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
            onStartGame = {
                navController.navigate(Game) {
                    popUpTo<Home>()
                }
            }
        )
    }
    composable<Game> {
        val session = Store.sessions[0]
        when (session.type) {
            GameType.FREE -> FreeGameScreen(
                viewModel = viewModel(
                    factory = FreeGameViewModel.getFactory(
                        gameName = "${session.name} (free)",
                        players = session.players
                    )
                )
            )
            GameType.ORDERED -> OrderedGameScreen(
                viewModel = viewModel(
                    factory = OrderedGameViewModel.getFactory(
                        gameName = "${session.name} (ordered)",
                        players = session.players
                    )
                )
            )
            else -> {}
        }
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
