package org.vl4ds4m.board.game.assistant.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.vl4ds4m.board.game.assistant.ui.game.GameModifier
import org.vl4ds4m.board.game.assistant.ui.game.gameNavigation
import org.vl4ds4m.board.game.assistant.ui.home.Home
import org.vl4ds4m.board.game.assistant.ui.profile.Profile
import org.vl4ds4m.board.game.assistant.ui.results.Results
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

interface Route

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    LaunchedEffect(Unit) { logCurrentBackStack(navController) }
    val currentEntry = navController.currentBackStackEntryAsState()
    val onMainScreen: State<Boolean> = remember {
        derivedStateOf {
            listOf(Home, Results, Profile).any {
                isCurrentDestination(currentEntry, it)
            }
        }
    }
    MainScreenContent(
        navController = navController,
        onMainScreen = onMainScreen,
        isNavItemSelected = { isCurrentDestination(currentEntry, it) }
    )
}

@Composable
fun MainScreenContent(
    navController: NavHostController,
    onMainScreen: State<Boolean>,
    isNavItemSelected: (Route) -> Boolean,
) {
    val topAppBarText = rememberSaveable { mutableStateOf("FallBack") }
    Scaffold(
        topBar = {
            AnimatedVisibility(!onMainScreen.value) {
                AppTopBar(
                    title = topAppBarText,
                    onArrowBackClick = { navController.navigateUp() },
                    onMenuClick = {}
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(onMainScreen.value) {
                MainNavBar(
                    selected = isNavItemSelected,
                    onClick = { navController.navigateToTop(it) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            mainNavigation(navController)
            gameNavigation(
                navController = navController,
                gameModifier = GameModifier(
                    topAppBarText = topAppBarText
                )
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    BoardGameAssistantTheme {
        MainScreen()
    }
}

private fun isCurrentDestination(
    currentEntry: State<NavBackStackEntry?>,
    route: Route
): Boolean {
    return currentEntry.value?.destination?.hasRoute(route::class) ?: false
}

private fun <T : Route> NavController.navigateToTop(route: T) {
    navigate(route) {
        popUpTo<Home> { saveState = true }
        restoreState = true
    }
}

@SuppressLint("RestrictedApi")
private suspend fun logCurrentBackStack(navController: NavHostController) {
    navController.currentBackStack.collect { stack ->
        val msg = buildString {
            append("Current back stack")
            if (stack.isEmpty()) {
                append(" is empty")
            } else {
                append(":\n")
                stack.forEach { append(it); append("\n") }
            }
        }
        Log.d("Navigation", msg)
    }
}
