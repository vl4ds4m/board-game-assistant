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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

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
    isNavItemSelected: (MainRoute) -> Boolean,
) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(onMainScreen.value) {
                MainNavBar(
                    isRouteSelected = isNavItemSelected,
                    onRouteNavigate = { navController.navigateToTop(it) }
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
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    BoardGameAssistantTheme {
        MainScreenContent(
            navController = rememberNavController(),
            onMainScreen = remember { mutableStateOf(true) },
            isNavItemSelected = { Home::class.isInstance(it) }
        )
    }
}

private fun isCurrentDestination(
    currentEntry: State<NavBackStackEntry?>,
    route: MainRoute
): Boolean {
    return currentEntry.value?.destination?.hasRoute(route::class) ?: false
}

private fun <T : MainRoute> NavController.navigateToTop(route: T) {
    navigate(route) {
        popUpTo<Home>()
        launchSingleTop = true
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
