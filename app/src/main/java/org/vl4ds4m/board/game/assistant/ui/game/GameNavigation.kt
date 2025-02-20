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
import org.vl4ds4m.board.game.assistant.ui.game.component.GameMenuActions
import org.vl4ds4m.board.game.assistant.ui.game.end.EndGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.setting.GameSettingScreen
import org.vl4ds4m.board.game.assistant.ui.game.setting.PlayerSettingScreen
import org.vl4ds4m.board.game.assistant.ui.game.setup.NewGamePlayersScreen
import org.vl4ds4m.board.game.assistant.ui.game.setup.NewGameStartScreen

sealed interface GameRoute

@Serializable
data object NewGameStart : GameRoute

@Serializable
data object NewGamePlayers : GameRoute

@Serializable
data class Game(val type: String, val sessionId: Long? = null) : GameRoute

@Serializable
data object GameSetting : GameRoute

@Serializable
data object PlayerSetting : GameRoute

@Serializable
data object End : GameRoute

fun NavGraphBuilder.gameNavigation(navController: NavController) {
    val onBackClick: () -> Unit = { navController.navigateUp() }
    composable<NewGameStart> {
        NewGameStartScreen(
            viewModel = viewModel(),
            onBackClick = onBackClick,
            onSetupPlayers = {
                navController.navigate(NewGamePlayers)
            }
        )
    }
    composable<NewGamePlayers> { entry ->
        val gameStartEntry = navController.rememberTopmost<NewGameStart>(entry)
        NewGamePlayersScreen(
            viewModel = viewModel(gameStartEntry),
            onBackClick = onBackClick,
            onStartGame = { type ->
                val game = Game(type.title)
                navController.navigate(game) {
                    popUpTo<Home>()
                }
            }
        )
    }
    composable<Game> { entry ->
        GameScreen(
            game = entry.toRoute<Game>(),
            menuActions = GameMenuActions(
                onBackClick = onBackClick,
                onGameSettingOpen = { navController.navigate(GameSetting) },
                onPlayerSettingOpen = { navController.navigate(PlayerSetting) },
                onGameComplete = { navController.navigate(End) },
            )
        )
    }
    composable<GameSetting> { entry ->
        val gameEntry = navController.rememberTopmost<Game>(entry)
        GameSettingScreen(
            viewModel = viewModel(gameEntry),
            onBackClick = onBackClick
        )
    }
    composable<PlayerSetting> { entry ->
        val gameEntry = navController.rememberTopmost<Game>(entry)
        PlayerSettingScreen(
            viewModel = viewModel(gameEntry),
            onBackClick = onBackClick
        )
    }
    composable<End> { entry ->
        val gameEntry = navController.rememberTopmost<Game>(entry)
        EndGameScreen(
            viewModel = viewModel(gameEntry),
            onBackClick = onBackClick,
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
