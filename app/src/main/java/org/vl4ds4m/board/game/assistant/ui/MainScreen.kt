package org.vl4ds4m.board.game.assistant.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.vl4ds4m.board.game.assistant.ui.home.HomeContent
import org.vl4ds4m.board.game.assistant.ui.profile.ProfileContent
import org.vl4ds4m.board.game.assistant.ui.results.ResultsContent

@Composable
fun MainContent() {
    val navController = rememberNavController()
    var currentScreen by rememberSaveable(stateSaver = Route.Saver) {
        mutableStateOf(startDestination)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { MainNavBar(
            onHomeClick = { navController.navigateTo(Home); currentScreen = Home },
            onResultsClick = { navController.navigateTo(Results); currentScreen = Results },
            onProfileClick = { navController.navigateTo(Profile); currentScreen = Profile },
            selected = currentScreen
        ) }
    ) { innerPadding ->
        MainNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MainNavBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onResultsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    selected: Route = startDestination
) {
    NavigationBar(modifier) {
        NavigationBarItem(
            selected = selected is Results,
            onClick = onResultsClick,
            icon = { Icon(
                imageVector = Icons.AutoMirrored.Default.List,
                contentDescription = null
            ) },
            label = { Text("Results") }
        )
        NavigationBarItem(
            selected = selected is Home,
            onClick = onHomeClick,
            icon = { Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null
            ) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selected is Profile,
            onClick = onProfileClick,
            icon = { Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            ) },
            label = { Text("Profile") }
        )
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: Route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize()
    ) {
        composable<Home> {
            HomeContent(listOf())
        }
        composable<Results> {
            ResultsContent()
        }
        composable<Profile> {
            ProfileContent()
        }
    }
}

private val startDestination: Route = Home

private fun <T : Route> NavController.navigateTo(route: T) {
    this.navigate(route) {
        popUpTo(startDestination) { saveState = true }
        restoreState = true
    }
}
