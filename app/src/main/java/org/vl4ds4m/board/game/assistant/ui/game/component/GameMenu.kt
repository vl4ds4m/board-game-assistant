package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

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
class GameMenuActions(
    val onGameSettingOpen: () -> Unit,
    val onGameComplete: () -> Unit
)
