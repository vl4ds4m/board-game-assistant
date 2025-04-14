package org.vl4ds4m.board.game.assistant.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.ui.Play
import org.vl4ds4m.board.game.assistant.ui.Profile
import org.vl4ds4m.board.game.assistant.ui.Results
import org.vl4ds4m.board.game.assistant.ui.TopRoute
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun MainNavBar(
    isRouteSelected: (TopRoute) -> Boolean,
    onRouteNavigate: (TopRoute) -> Unit,
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
                selected = isRouteSelected(dest.route),
                onClick = { onRouteNavigate(dest.route) }
            )
        }
    }
}

private class TopLevelDestination(
    val route: TopRoute,
    val imageVector: ImageVector,
    val label: String
)

private val topLevelDestinations = listOf(
    TopLevelDestination(Results, Icons.AutoMirrored.Default.List, "Results"),
    TopLevelDestination(Play, Icons.Default.Home, "Play"),
    TopLevelDestination(Profile, Icons.Default.Person, "Profile")
)

@Preview
@Composable
private fun MainNavBarPreview() {
    BoardGameAssistantTheme {
        MainNavBar(
            isRouteSelected = { it is Play },
            onRouteNavigate = {},
        )
    }
}
