package org.vl4ds4m.board.game.assistant.ui.game

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.gameNavigation(navController: NavController) {
    composable<NewGameStart> {
        NewGameStartContent(
            onSetupPlayers = { navController.navigate(NewGamePlayers) }
        )
    }
    composable<NewGamePlayers> {
        NewGamePlayersContent(
            viewModel = viewModel( // TODO Find out why this code is called on exit of this composable
                viewModelStoreOwner = navController.getBackStackEntry(NewGameStart)
            )
        )
    }
}
