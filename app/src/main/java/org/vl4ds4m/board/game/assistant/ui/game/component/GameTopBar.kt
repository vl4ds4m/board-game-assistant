package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(
    title: String,
    onArrowBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    menuActions: GameMenuActions? = null
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
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
            if (menuActions != null) {
                val expanded = remember { mutableStateOf(false) }
                IconButton(
                    onClick = { expanded.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu"
                    )
                    GameMenu(
                        expanded = expanded,
                        actions = menuActions
                    )
                }
            }
        }
    )
}

@Composable
fun GameMenu(
    expanded: MutableState<Boolean>,
    actions: GameMenuActions,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = { Text("Open game settings") },
            onClick = actions.onGameSettingOpen
        )
        DropdownMenuItem(
            text = { Text("Complete game") },
            onClick = actions.onGameComplete
        )
    }
}

@Immutable
data class GameMenuActions(
    val onBackClick: () -> Unit,
    val onGameSettingOpen: () -> Unit,
    val onGameComplete: () -> Unit
)
