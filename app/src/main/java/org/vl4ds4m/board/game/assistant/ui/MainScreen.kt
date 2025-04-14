package org.vl4ds4m.board.game.assistant.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.vl4ds4m.board.game.assistant.ui.component.MainNavBar
import org.vl4ds4m.board.game.assistant.ui.component.MainTopBar
import org.vl4ds4m.board.game.assistant.ui.component.TopBarParams
import org.vl4ds4m.board.game.assistant.ui.game.gameNavigation
import org.vl4ds4m.board.game.assistant.ui.game.observer.observerNavigation
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    LaunchedEffect(Unit) { logCurrentBackStack(navController) }
    val currentEntry = navController.currentBackStackEntryAsState()
    val onTopScreen = remember {
        derivedStateOf {
            listOf(Play, Results, Profile).any {
                isCurrentDestination(currentEntry, it)
            }
        }
    }
    val topBarUiState = remember {
        mutableStateOf(TopBarParams.Empty)
    }
    MainScreenContent(
        navController = navController,
        onTopScreen = onTopScreen,
        isCurrentRoute = { isCurrentDestination(currentEntry, it) },
        topBarUiState = topBarUiState
    ) {
        topNavigation(navController, topBarUiState)
        gameNavigation(navController, topBarUiState)
        observerNavigation(navController, topBarUiState)
    }
}

@Composable
fun MainScreenContent(
    navController: NavHostController,
    onTopScreen: State<Boolean>,
    isCurrentRoute: (TopRoute) -> Boolean,
    topBarUiState: MutableState<TopBarParams>,
    builder: NavGraphBuilder.() -> Unit
) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(onTopScreen.value) {
                MainNavBar(
                    isRouteSelected = isCurrentRoute,
                    onRouteNavigate = { navController.navigateToTop(it) }
                )
            }
        },
        topBar = {
            AnimatedVisibility(!onTopScreen.value) {
                MainTopBar(topBarUiState)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Play,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            builder = builder
        )
    }
}

@Composable
inline fun <reified T : Any> NavController.rememberTopmost(
    key: NavBackStackEntry
): NavBackStackEntry = remember(key) {
    Log.d("Navigation", "Remember ${T::class.simpleName} navBackStackEntry")
    getBackStackEntry<T>()
}

private fun isCurrentDestination(
    currentEntry: State<NavBackStackEntry?>,
    route: TopRoute
): Boolean {
    return currentEntry.value?.destination?.hasRoute(route::class) ?: false
}

private fun <T : TopRoute> NavController.navigateToTop(route: T) {
    navigate(route) {
        popUpTo<Play>()
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

@Preview
@Composable
private fun MainTopScreenPreview() {
    MainScreenPreview(true)
}

@Preview
@Composable
private fun MainAnotherScreenPreview() {
    MainScreenPreview(false)
}

@Composable
private fun MainScreenPreview(onTopScreen: Boolean) {
    BoardGameAssistantTheme {
        MainScreenContent(
            navController = rememberNavController(),
            onTopScreen = remember { mutableStateOf(onTopScreen) },
            isCurrentRoute = { onTopScreen && (it is Play) },
            topBarUiState = remember {
                mutableStateOf(TopBarParams.Example)
            }
        ) {
            composable<Play> {
                Text(
                    text = "Screen content",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
        }
    }
}
