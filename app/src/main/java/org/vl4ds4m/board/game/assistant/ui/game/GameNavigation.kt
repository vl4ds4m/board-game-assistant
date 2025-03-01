package org.vl4ds4m.board.game.assistant.ui.game

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.ui.Home
import org.vl4ds4m.board.game.assistant.ui.game.component.DiceImitationScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.end.EndGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.setting.GameSettingScreen
import org.vl4ds4m.board.game.assistant.ui.game.setting.PlayerSettingScreen
import org.vl4ds4m.board.game.assistant.ui.game.setup.GameSetupViewModel
import org.vl4ds4m.board.game.assistant.ui.game.setup.NewGamePlayersScreen
import org.vl4ds4m.board.game.assistant.ui.game.setup.NewGameStartScreen
import org.vl4ds4m.board.game.assistant.ui.rememberTopmost

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
data object DiceImitation : GameRoute

@Serializable
data object End : GameRoute

fun NavGraphBuilder.gameNavigation(navController: NavController) {
    val navigateUp: () -> Unit = { navController.navigateUp() }
    composable<NewGameStart> {
        NewGameStartScreen(
            viewModel = viewModel(factory = GameSetupViewModel.Factory),
            onBackClick = navigateUp,
            onSetupPlayers = {
                navController.navigate(NewGamePlayers)
            }
        )
    }
    composable<NewGamePlayers> { entry ->
        val gameStartEntry = navController.rememberTopmost<NewGameStart>(entry)
        NewGamePlayersScreen(
            viewModel = viewModel(gameStartEntry),
            onBackClick = navigateUp,
            onStartGame = { type ->
                val game = Game(type.title)
                navController.navigate(game) {
                    popUpTo<Home>()
                }
            }
        )
    }
    composable<Game> { entry ->
        val (type, sessionId) = entry.toRoute<Game>()
            .run { GameType.valueOf(type) to sessionId }
        GameScreen(
            type = type,
            sessionId = sessionId,
            navActions = GameNavActions(
                onBackClick = navigateUp,
                onGameSettingOpen = { navController.navigate(GameSetting) },
                onPlayerSettingOpen = { navController.navigate(PlayerSetting) },
                navigateDiceImitation = { navController.navigate(DiceImitation) },
                onGameComplete = { navController.navigate(End) },
            )
        )
    }
    composable<GameSetting> { entry ->
        val gameEntry = navController.rememberTopmost<Game>(entry)
        GameSettingScreen(
            viewModel = viewModel(gameEntry),
            onBackClick = navigateUp
        )
    }
    composable<PlayerSetting> { entry ->
        val gameEntry = navController.rememberTopmost<Game>(entry)
        PlayerSettingScreen(
            viewModel = viewModel(gameEntry),
            onBackClick = navigateUp
        )
    }
    composable<DiceImitation> {
        DiceImitationScreen(navigateUp)
    }
    composable<End> { entry ->
        val gameEntry = navController.rememberTopmost<Game>(entry)
        EndGameScreen(
            viewModel = viewModel(gameEntry),
            onBackClick = navigateUp,
            onHomeNavigate = {
                navController.navigate(Home) {
                    popUpTo<Home>()
                    launchSingleTop = true
                }
            }
        )
    }
}
