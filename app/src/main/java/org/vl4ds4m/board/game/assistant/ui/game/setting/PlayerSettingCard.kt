package org.vl4ds4m.board.game.assistant.ui.game.setting

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayerSettingCard(
    id: Long,
    name: String,
    active: Boolean,
    selected: Boolean,
    menuActions: PlayerSettingActions,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = with(MaterialTheme.colorScheme) {
                if (active) {
                    if (selected) surfaceVariant
                    else surfaceContainerLow
                } else {
                    this.surfaceContainerLowest
                }
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Player setting",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(24.dp))
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Player image",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(16.dp))
            TextField(
                value = name,
                onValueChange = { menuActions.onRename(id, it) },
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.titleMedium,
                singleLine = true
            )
            Spacer(Modifier.width(24.dp))
            val expanded = remember { mutableStateOf(false) }
            IconButton(
                onClick = { expanded.value = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Player setting",
                    modifier = Modifier.size(32.dp)
                )
                PlayerSettingMenu(
                    id = id,
                    active = active,
                    selected = selected,
                    expanded = expanded,
                    actions = menuActions
                )
            }
        }
    }
}

@Composable
fun PlayerSettingMenu(
    id: Long,
    active: Boolean,
    selected: Boolean,
    expanded: MutableState<Boolean>,
    actions: PlayerSettingActions,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = modifier
    ) {
        buildList {
            if (!selected) {
                add("Make current" to actions.onSelect)
            }
            if (active) {
                add("Freeze player" to actions.onFreeze)
            } else {
                add("Unfreeze player" to actions.onUnfreeze)
            }
            add("Remove player" to actions.onRemove)
        }.forEach { (text, action) ->
            DropdownMenuItem(
                text = { Text(text) },
                onClick = {
                    expanded.value = false
                    action(id)
                }
            )
        }
    }
}

@Immutable
class PlayerSettingActions(
    val onSelect: (Long) -> Unit,
    val onRename: (Long, String) -> Unit,
    val onRemove: (Long) -> Unit,
    val onFreeze: (Long) -> Unit,
    val onUnfreeze: (Long) -> Unit
) {
    companion object {
        val Empty = PlayerSettingActions(
            {}, { _, _ -> }, {}, {}, {}
        )
    }
}

@Composable
private fun PlayerSettingCardPreview(
    active: Boolean,
    selected: Boolean
) {
    BoardGameAssistantTheme {
        PlayerSettingCard(
            id = 1,
            name = "Hello",
            active = active,
            selected = selected,
            menuActions = PlayerSettingActions.Empty
        )
    }
}

@Preview
@Composable
private fun PlayerSettingCardPreviewSelected() {
    PlayerSettingCardPreview(
        active = true,
        selected = true
    )
}

@Preview
@Composable
private fun PlayerSettingCardPreviewActive() {
    PlayerSettingCardPreview(
        active = true,
        selected = false
    )
}

@Preview
@Composable
private fun PlayerSettingCardPreviewInactive() {
    PlayerSettingCardPreview(
        active = false,
        selected = true
    )
}
