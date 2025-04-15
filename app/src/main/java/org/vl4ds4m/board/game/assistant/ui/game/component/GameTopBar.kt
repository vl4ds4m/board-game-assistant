package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import org.vl4ds4m.board.game.assistant.R

@Composable
fun GameHistoryManager(state: GameHistoryState) {
    IconButton(
        enabled = state.reverted.value,
        onClick = state.revert
    ) {
        Icon(
            painter = painterResource(R.drawable.undo_24px),
            contentDescription = "Revert"
        )
    }
    IconButton(
        enabled = state.repeatable.value,
        onClick = state.repeat
    ) {
        Icon(
            painter = painterResource(R.drawable.redo_24px),
            contentDescription = "Repeat"
        )
    }
}

@Composable
fun GameMenu(actions: GameNavActions) {
    val expanded = remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded.value = true }
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Menu"
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            listOf(
                "Open game settings"   to actions.openGameSetting,
                "Open player settings" to actions.openPlayerSetting,
                "Open Dice imitation"  to actions.openDiceImitation,
                "Complete game"        to actions.completeGame,
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
}

@Immutable
data class GameNavActions(
    val navigateBack: () -> Unit,
    val openGameSetting: () -> Unit,
    val openPlayerSetting: () -> Unit,
    val openDiceImitation: () -> Unit,
    val completeGame: () -> Unit
)

@Immutable
data class GameHistoryState(
    val reverted: State<Boolean>,
    val repeatable: State<Boolean>,
    val revert: () -> Unit,
    val repeat: () -> Unit
)
