package org.vl4ds4m.board.game.assistant.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.vl4ds4m.board.game.assistant.data.Store
import org.vl4ds4m.board.game.assistant.ui.game.NewGameStart
import org.vl4ds4m.board.game.assistant.ui.game.gameNavigation
import org.vl4ds4m.board.game.assistant.ui.home.Home
import org.vl4ds4m.board.game.assistant.ui.home.HomeContent
import org.vl4ds4m.board.game.assistant.ui.profile.Profile
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileContent
import org.vl4ds4m.board.game.assistant.ui.results.Results
import org.vl4ds4m.board.game.assistant.ui.results.ResultsContent
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun MainContent() {
    val navController = rememberNavController()
    val currentDest = navController.currentBackStackEntryAsState()
        .value?.destination
    val topLevelDest = topLevelDestinations.any {
        currentDest?.hasRoute(it.route::class) ?: false
    }
    Scaffold(
        bottomBar = {
            AnimatedVisibility(topLevelDest) {
                MainNavBar(
                    selected = { currentDest?.hasRoute(it::class) ?: false },
                    onClick = { navController.navigateToTop(it) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // top level screens
            composable<Home> {
                HomeContent(
                    sessions = Store.sessions.map { it.name },
                    onStart = { navController.navigate(NewGameStart) }
                )
            }
            composable<Results> {
                ResultsContent()
            }
            composable<Profile> {
                ProfileContent()
            }

            // game screens
            gameNavigation(navController)
        }
    }
}

@Composable
fun MainNavBar(
    selected: (Any) -> Boolean,
    onClick: (Any) -> Unit,
    modifier: Modifier = Modifier
) = NavigationBar(modifier) {
    topLevelDestinations.forEach { dest ->
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = dest.imageVector,
                    contentDescription = null
                )
            },
            label = { Text(dest.label) },
            selected = selected(dest.route),
            onClick = { onClick(dest.route) }
        )
    }
}

@Preview
@Composable
private fun MainActivityViewPreview() {
    BoardGameAssistantTheme {
        MainContent()
    }
}

private class TopLevelDestination(
    val route: Any,
    val imageVector: ImageVector,
    val label: String
)

private val topLevelDestinations = listOf(
    TopLevelDestination(Results, Icons.AutoMirrored.Default.List, "Results"),
    TopLevelDestination(Home, Icons.Default.Home, "Home"),
    TopLevelDestination(Profile, Icons.Default.Person, "Profile")
)

private val startDestination = Home

private fun <T : Any> NavController.navigateToTop(route: T) {
    navigate(route) {
        popUpTo(startDestination) { saveState = true }
        restoreState = true
    }
}
