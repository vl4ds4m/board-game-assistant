package org.vl4ds4m.board.game.assistant.ui.results

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.ui.Results
import org.vl4ds4m.board.game.assistant.ui.component.TopBarUiState
import org.vl4ds4m.board.game.assistant.ui.rememberTopmost

@Serializable
data class CompletedGame(val sessionId: String)

fun NavGraphBuilder.resultsNavigation(
    navController: NavController,
    topBarUiState: TopBarUiState
) {
    composable<Results> {
        ResultsScreen(
            viewModel = viewModel(factory = ResultsViewModel.Factory),
            clickSession = { sessionId ->
                navController.navigate(CompletedGame(sessionId))
            }
        )
    }
    composable<CompletedGame> { entry ->
        val sessionId = entry.toRoute<CompletedGame>().sessionId
        val resultsEntry = navController.rememberTopmost<Results>(entry)
        CompletedGameScreen(
            topBarUiState = topBarUiState,
            viewModel = viewModel(resultsEntry),
            sessionId = sessionId,
            navigateBack = { navController.navigateUp() }
        )
    }
}
