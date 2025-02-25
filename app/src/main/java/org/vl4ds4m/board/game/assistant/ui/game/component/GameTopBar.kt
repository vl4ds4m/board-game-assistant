package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(
    title: String,
    onArrowBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    navActions: GameNavActions? = null,
    history: GameHistory? = null
) {
    TopAppBar(
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
            history?.let {
                IconButton(
                    enabled = it.reverted.value,
                    onClick = it.onRevertAction
                ) {
                    Icon(
                        painter = painterResource(R.drawable.undo_24px),
                        contentDescription = "Revert"
                    )
                }
                IconButton(
                    enabled = it.repeatable.value,
                    onClick = it.onRepeatAction
                ) {
                    Icon(
                        painter = painterResource(R.drawable.redo_24px),
                        contentDescription = "Repeat"
                    )
                }
            }
            navActions?.let { actions ->
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
                        actions = actions
                    )
                }
            }
        }
    )
}

@Composable
fun GameMenu(
    expanded: MutableState<Boolean>,
    actions: GameNavActions,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = modifier
    ) {
        listOf(
            "Open game settings"   to actions.onGameSettingOpen,
            "Open player settings" to actions.onPlayerSettingOpen,
            "Complete game"        to actions.onGameComplete,
        ).forEach { (text, action) ->
            DropdownMenuItem(
                text = { Text(text) },
                onClick = {
                    expanded.value = false
                    action()
                }
            )
        }
    }
}

@Preview
@Composable
private fun GameTopBarPreview() {
    BoardGameAssistantTheme {
        GameTopBar(
            title = "Some game",
            onArrowBackClick = {},
            navActions = GameNavActions.Empty,
            history = GameHistory.Empty
        )
    }
}

@Immutable
data class GameNavActions(
    val onBackClick: () -> Unit,
    val onGameSettingOpen: () -> Unit,
    val onPlayerSettingOpen: () -> Unit,
    val onGameComplete: () -> Unit
) {
    companion object {
        val Empty = GameNavActions(
            onBackClick = {},
            onGameSettingOpen = {},
            onPlayerSettingOpen = {},
            onGameComplete = {}
        )
    }
}

@Immutable
data class GameHistory(
    val reverted: State<Boolean>,
    val repeatable: State<Boolean>,
    val onRevertAction: () -> Unit,
    val onRepeatAction: () -> Unit
) {
    companion object {
        val Empty = GameHistory(
            reverted = mutableStateOf(false),
            repeatable = mutableStateOf(false),
            onRevertAction = {}, onRepeatAction = {}
        )
    }
}
