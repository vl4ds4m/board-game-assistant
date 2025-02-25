package org.vl4ds4m.board.game.assistant.ui.results

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.ui.Results

@Serializable
data class CompletedGame(val sessionId: Long)

fun NavGraphBuilder.resultsNavigation(navController: NavController) {
    composable<Results> {
        ResultsScreen(
            sessions = Store.sessions,
            onSessionClick = { sessionId ->
                val route = CompletedGame(sessionId)
                navController.navigate(route)
            }
        )
    }
    composable<CompletedGame> { entry ->
        val session = entry.toRoute<CompletedGame>().sessionId.let {
            Store.load(it) ?: run {
                Log.e(
                    "GameResults",
                    "Can't load completed session[id = $it]"
                )
                return@composable
            }
        }
        CompletedGameScreen(
            name = session.name,
            players = session.players,
            navigateUp = { navController.navigateUp() }
        )
    }
}
