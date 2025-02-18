package org.vl4ds4m.board.game.assistant.ui.game

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.ui.Home
import org.vl4ds4m.board.game.assistant.ui.game.component.GameMenuActions
import org.vl4ds4m.board.game.assistant.ui.game.end.EndGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.setting.GameSettingScreen
import org.vl4ds4m.board.game.assistant.ui.game.start.NewGamePlayersScreen
import org.vl4ds4m.board.game.assistant.ui.game.start.NewGameStartScreen

sealed interface GameRoute

@Serializable
data object NewGameStart : GameRoute

@Serializable
data object NewGamePlayers : GameRoute

@Serializable
data class Game(val type: String, val sessionId: Long? = null) : GameRoute

@Serializable
data object GameSetting : GameRoute

/*@Serializable
data object PlayerSetting : GameRoute*/

@Serializable
data object End : GameRoute

fun NavGraphBuilder.gameNavigation(navController: NavController) {
    gameComposable<NewGameStart>(
        navController = navController,
        topBarTitle = mutableStateOf("New game")
    ) { _, modifier ->
        NewGameStartScreen(
            viewModel = viewModel(),
            onSetupPlayers = {
                navController.navigate(NewGamePlayers)
            },
            modifier = modifier
        )
    }
    gameComposable<NewGamePlayers>(
        navController = navController,
        topBarTitle = mutableStateOf("New game")
    ) { entry, modifier ->
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
            },
            modifier = modifier
        )
    }
    val gameModifier = GameModifier(
        topBarTitle = mutableStateOf(""),
    )
    gameComposable<Game>(
        navController = navController,
        topBarTitle = gameModifier.topBarTitle,
        gameMenuActions = GameMenuActions(
            onGameSettingOpen = { navController.navigate(GameSetting) },
            onGameComplete = { navController.navigate(End) }
        )
    ) { entry, modifier ->
        GameScreen(
            game = entry.toRoute<Game>(),
            gameModifier = gameModifier,
            modifier = modifier
        )
    }
    gameComposable<GameSetting>(
        navController = navController,
        topBarTitle = mutableStateOf("Game settings")
    ) { entry, modifier ->
        val backStackEntry = navController.rememberTopmost<Game>(entry)
        GameSettingScreen(
            viewModel = viewModel(
                viewModelStoreOwner = backStackEntry
            ),
            modifier = modifier
        )
    }
    gameComposable<End>(
        navController = navController,
        topBarTitle = mutableStateOf("Game end")
    ) { _, modifier ->
        EndGameScreen(
            onHomeNavigate = {
                navController.navigate(Home) {
                    popUpTo<Home>()
                    launchSingleTop = true
                }
            },
            modifier = modifier
        )
    }
}

private inline fun <reified T : GameRoute> NavGraphBuilder.gameComposable(
    navController: NavController,
    topBarTitle: State<String>,
    gameMenuActions: GameMenuActions? = null,
    noinline content: @Composable (NavBackStackEntry, Modifier) -> Unit
) {
    composable<T>{ navBackStackEntry ->
        GameScreen(
            entry = navBackStackEntry,
            topBarTitle = topBarTitle,
            onBackClick = { navController.navigateUp() },
            menuActions = gameMenuActions,
            content = content
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
