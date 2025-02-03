package org.vl4ds4m.board.game.assistant.ui

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
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
    val isNavItemSelected: (Any) -> Boolean = {
        currentEntry.value?.destination?.hasRoute(it::class) ?: false
    }
    val visibleNavBar: State<Boolean> = remember {
        derivedStateOf {
            topLevelDestinations.any {
                currentEntry.value?.destination?.hasRoute(it.route::class) ?: false
            }
        }
    }
    MainScreenContent(
        navController = navController,
        visibleNavBar = visibleNavBar,
        isNavItemSelected = isNavItemSelected
    )
}

@Composable
fun MainScreenContent(
    navController: NavHostController,
    visibleNavBar: State<Boolean>,
    isNavItemSelected: (Any) -> Boolean,
) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(visibleNavBar.value) {
                MainNavBar(
                    selected = isNavItemSelected,
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
            mainNavigation(navController)
        }
    }
}

@Composable
fun MainNavBar(
    selected: (Any) -> Boolean,
    onClick: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier) {
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
}

@Preview
@Composable
private fun MainScreenPreview() {
    BoardGameAssistantTheme {
        MainScreen()
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
