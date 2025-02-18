package org.vl4ds4m.board.game.assistant.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.vl4ds4m.board.game.assistant.ui.home.Home
import org.vl4ds4m.board.game.assistant.ui.profile.Profile
import org.vl4ds4m.board.game.assistant.ui.results.Results

@Composable
fun MainNavBar(
    selected: (Route) -> Boolean,
    onClick: (Route) -> Unit,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: State<String>,
    onArrowBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title.value) },
        navigationIcon = {
            IconButton(
                onClick = onArrowBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        }
    )
}

private class TopLevelDestination(
    val route: Route,
    val imageVector: ImageVector,
    val label: String
)

private val topLevelDestinations = listOf(
    TopLevelDestination(Results, Icons.AutoMirrored.Default.List, "Results"),
    TopLevelDestination(Home, Icons.Default.Home, "Home"),
    TopLevelDestination(Profile, Icons.Default.Person, "Profile")
)
