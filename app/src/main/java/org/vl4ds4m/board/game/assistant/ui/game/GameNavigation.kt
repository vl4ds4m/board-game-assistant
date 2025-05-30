package org.vl4ds4m.board.game.assistant.ui.game

import androidx.activity.addCallback
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.R
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
            title = stringResource(R.string.new_game_title),
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
            title = stringResource(R.string.new_game_title),
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
        val (type, sessionId) = entry.toRoute<Game>()
            .run { GameType.valueOf(type) to sessionId }
        GameScreen(
            type = type,
            sessionId = sessionId,
            topBarUiState = topBarUiState,
            navActions = GameNavActions(
                navigateBack = navigateHome,
                openGameSetting = { navController.navigate(GameSetting) },
                openPlayerSetting = { navController.navigate(PlayerSetting) },
                openDiceImitation = { navController.navigate(DiceImitation) },
                completeGame = { navController.navigate(End) },
            )
        )
    }
    composable<GameSetting> { entry ->
        topBarUiState.update(
            title = stringResource(R.string.game_settings_title),
            navigateBack = navigateUp
        )
        val gameEntry = navController.rememberTopmost<Game>(entry)
        CompositionLocalProvider(LocalViewModelStoreOwner provides gameEntry) {
            GameSettingScreen()
        }
    }
    composable<PlayerSetting> { entry ->
        topBarUiState.update(
            title = stringResource(R.string.game_players_settings_title),
            navigateBack = navigateUp
        )
        val gameEntry = navController.rememberTopmost<Game>(entry)
        CompositionLocalProvider(LocalViewModelStoreOwner provides gameEntry) {
            PlayerSettingScreen()
        }
    }
    composable<DiceImitation> {
        topBarUiState.update(
            title = stringResource(R.string.game_dice_imitation_title),
            navigateBack = navigateUp
        )
        DiceImitationScreen()
    }
    composable<End> { entry ->
        val gameEntry = navController.rememberTopmost<Game>(entry)
        val viewModel = viewModel<GameViewModel>(gameEntry)
        val returnGame = {
            viewModel.returnGame()
            navigateUp()
        }
        topBarUiState.update(
            title = stringResource(R.string.game_end_title),
            navigateBack = returnGame
        )
        LocalActivity.current.let {
            it as MainActivity
        }.onBackPressedDispatcher.addCallback(
            owner = LocalLifecycleOwner.current
        ) {
            returnGame()
        }
        CompositionLocalProvider(LocalViewModelStoreOwner provides gameEntry) {
            EndGameScreen(navigateHome)
        }
    }
}
