package org.vl4ds4m.board.game.assistant.ui.game

import androidx.activity.addCallback
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.ui.Play
import org.vl4ds4m.board.game.assistant.ui.MainActivity
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState
import org.vl4ds4m.board.game.assistant.ui.game.component.DiceImitationScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.end.EndGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.setting.GameSettingScreen
import org.vl4ds4m.board.game.assistant.ui.game.setting.PlayerSettingScreen
import org.vl4ds4m.board.game.assistant.ui.game.setup.GameSetupViewModel
import org.vl4ds4m.board.game.assistant.ui.game.setup.NewGamePlayersScreen
import org.vl4ds4m.board.game.assistant.ui.game.setup.NewGameStartScreen
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.rememberTopmost

sealed interface GameRoute

@Serializable
data object NewGameStart : GameRoute

@Serializable
data object NewGamePlayers : GameRoute

@Serializable
data class Game(val type: String, val sessionId: String?) : GameRoute

@Serializable
data object GameSetting : GameRoute

@Serializable
data object PlayerSetting : GameRoute

@Serializable
data object DiceImitation : GameRoute

@Serializable
data object End : GameRoute

fun NavGraphBuilder.gameNavigation(
    navController: NavController,
    topBarUiState: TopBarUiState
) {
    val navigateUp: () -> Unit = { navController.navigateUp() }
    val navigateHome: () -> Unit = {
        navController.navigate(Play) {
            popUpTo<Play>()
            launchSingleTop = true
        }
    }
    composable<NewGameStart> {
        topBarUiState.update(
            title = "New game: creation",
            navigateBack = navigateUp
        )
        NewGameStartScreen(
            viewModel = viewModel(factory = GameSetupViewModel.Factory),
            onSetupPlayers = { type ->
                Game(type = type.title, sessionId = null).let {
                    navController.navigate(it)
                }
                navController.navigate(NewGamePlayers)
            }
        )
    }
    composable<NewGamePlayers> { entry ->
        val setupViewModel = viewModel<GameSetupViewModel>(
            navController.rememberTopmost<NewGameStart>(entry)
        )
        val gameViewModel = viewModel<GameViewModel>(
            viewModelStoreOwner = navController.rememberTopmost<Game>(entry),
            factory = GameViewModel.createFactory(
                sessionId = null,
                producer = setupViewModel.type.value!!.viewModelProducer
            )
        )
        val onBackClick: () -> Unit = {
            navController.navigate(NewGameStart) {
                popUpTo<NewGameStart>()
                launchSingleTop = true
            }
        }
        topBarUiState.update(
            title = "New game: players",
            navigateBack = onBackClick
        )
        LocalActivity.current.let {
            it as MainActivity
        }.run {
            onBackPressedDispatcher.addCallback(entry) { onBackClick() }
        }
        NewGamePlayersScreen(
            viewModel = setupViewModel,
            gameViewModel = gameViewModel,
            onStartGame = {
                setupViewModel.startGame()
                navController.navigateUp()
            }
        )
    }
    composable<Game> { entry ->
        LocalActivity.current.let {
            it as MainActivity
        }.run {
            onBackPressedDispatcher.addCallback(entry) { navigateHome() }
        }
        val (type, sessionId) = entry.toRoute<Game>()
            .run { GameType.valueOf(type) to sessionId }
        val navActions = GameNavActions(
            navigateBack = navigateHome,
            openGameSetting = { navController.navigate(GameSetting) },
            openPlayerSetting = { navController.navigate(PlayerSetting) },
            openDiceImitation = { navController.navigate(DiceImitation) },
            completeGame = { navController.navigate(End) },
        )
        GameScreen(
            type = type,
            sessionId = sessionId,
            topBarUiState = topBarUiState,
            navActions = navActions
        )
    }
    composable<GameSetting> { entry ->
        topBarUiState.update(
            title = "Game settings",
            navigateBack = navigateUp
        )
        val gameEntry = navController.rememberTopmost<Game>(entry)
        CompositionLocalProvider(LocalViewModelStoreOwner provides gameEntry) {
            GameSettingScreen()
        }
    }
    composable<PlayerSetting> { entry ->
        topBarUiState.update(
            title = "Player Settings",
            navigateBack = navigateUp
        )
        val gameEntry = navController.rememberTopmost<Game>(entry)
        CompositionLocalProvider(LocalViewModelStoreOwner provides gameEntry) {
            PlayerSettingScreen()
        }
    }
    composable<DiceImitation> {
        topBarUiState.update(
            title = "Dice Imitation",
            navigateBack = navigateUp
        )
        DiceImitationScreen()
    }
    composable<End> { entry ->
        val gameEntry = navController.rememberTopmost<Game>(entry)
        val viewModel = viewModel<GameViewModel>(gameEntry)
        topBarUiState.update(
            title = "Game end",
            navigateBack = {
                viewModel.returnGame()
                navigateUp()
            }
        )
        EndGameScreen(navigateHome)
    }
}
