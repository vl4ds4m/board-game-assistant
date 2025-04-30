package org.vl4ds4m.board.game.assistant.ui.game.setting

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.component.PlayerCard
import org.vl4ds4m.board.game.assistant.ui.component.PlayerIcon
import org.vl4ds4m.board.game.assistant.ui.component.PlayerIndicators
import org.vl4ds4m.board.game.assistant.ui.component.PlayerName
import org.vl4ds4m.board.game.assistant.ui.component.PlayerPosition
import org.vl4ds4m.board.game.assistant.ui.component.PlayerState
import org.vl4ds4m.board.game.assistant.ui.component.isValidPlayerName
import org.vl4ds4m.board.game.assistant.ui.component.playerName
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayerSettingCard(
    id: Long,
    name: String,
    remote: Boolean,
    frozen: Boolean,
    selected: Boolean,
    settingActions: PlayerSettingActions,
    index: Int,
    playersCount: State<Int>,
    modifier: Modifier = Modifier
) {
    PlayerCard(
        selected = selected,
        modifier = modifier
    ) {
        PlayerPosition(index + 1)
        PlayerIcon(name)
        PlayerState(
            topRow = {
                PlayerName(
                    name = name,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            bottomRow = {
                if (remote || frozen) {
                    PlayerIndicators(
                        remote = remote,
                        frozen = frozen
                    )
                }
            },
            modifier = Modifier.weight(1f)
        )
        SettingButton(
            playerId = id,
            name = name,
            remote = remote,
            frozen = frozen,
            selected = selected,
            actions = settingActions,
            playersCount = playersCount
        )
    }
}

@Composable
private fun SettingButton(
    playerId: Long,
    name: String,
    remote: Boolean,
    frozen: Boolean,
    selected: Boolean,
    actions: PlayerSettingActions,
    playersCount: State<Int>
) {
    val expanded = remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded.value = true }
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Player setting",
            modifier = Modifier.size(24.dp)
        )
        val menu = actionsMenu(
            playerId = playerId,
            name = name,
            remote = remote,
            frozen = frozen,
            selected = selected,
            actions = actions,
            playersCount = playersCount
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            menu.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(stringResource(item.resId))
                    },
                    onClick = {
                        expanded.value = false
                        item.action()
                    }
                )
            }
        }
    }
}

@Composable
private fun actionsMenu(
    playerId: Long,
    name: String,
    remote: Boolean,
    frozen: Boolean,
    selected: Boolean,
    actions: PlayerSettingActions,
    playersCount: State<Int>
): List<Action> {
    val menu = mutableListOf<Action?>()
    menu += onOrderChangeAction(
        playersCount = playersCount,
        onOrderChange = actions.onOrderChange?.let { change ->
            { i: Int -> change(playerId, i) }
        }
    )
    menu += onSelectAction(
        frozen = frozen,
        selected = selected,
        onSelect = { actions.onSelect(playerId) }
    )
    menu += onRenameAction(name) { actions.onRename(playerId, it) }
    menu += playerActivityAction(
        frozen = frozen,
        onFreeze = { actions.onFreeze(playerId) },
        onUnfreeze = { actions.onUnfreeze(playerId) }
    )
    menu += onUnbindAction(remote) { actions.onUnbind(playerId) }
    menu += onRemoveAction(name) { actions.onRemove(playerId) }
    return menu.filterNotNull()
}

private data class Action(
    @StringRes val resId: Int,
    val action: () -> Unit
)

@Composable
private fun onOrderChangeAction(
    playersCount: State<Int>,
    onOrderChange: ((Int) -> Unit)?
): Action? {
    onOrderChange ?: return null
    val changeOrderExp = remember { mutableStateOf(false) }
    ChangeOrderMenu(
        expanded = changeOrderExp,
        playersCount = playersCount,
        onOrderChange = onOrderChange
    )
    return Action(R.string.player_action_change_order) {
        changeOrderExp.value = true
    }
}

private fun onSelectAction(
    frozen: Boolean,
    selected: Boolean,
    onSelect: () -> Unit
): Action? = Action(R.string.player_action_select, onSelect)
    .takeIf { !frozen && !selected }

@Composable
private fun onRenameAction(name: String, onRename: (String) -> Unit): Action {
    val renameExp = remember { mutableStateOf(false) }
    RenameDialog(
        opened = renameExp,
        oldName = name,
        rename = onRename
    )
    return Action(R.string.player_action_rename) {
        renameExp.value = true
    }
}

private fun playerActivityAction(
    frozen: Boolean,
    onFreeze: () -> Unit,
    onUnfreeze: () -> Unit
): Action = if (frozen) {
    Action(R.string.player_action_unfreeze, onUnfreeze)
} else {
    Action(R.string.player_action_freeze, onFreeze)
}

private fun onUnbindAction(remote: Boolean, onUnbind: () -> Unit): Action? =
    Action(R.string.player_action_unbind, onUnbind).takeIf { remote }

@Composable
private fun onRemoveAction(name: String, onRemove: () -> Unit): Action {
    val removeDialogOpened = remember { mutableStateOf(false) }
    RemoveDialog(
        opened = removeDialogOpened,
        name = name,
        remove = onRemove
    )
    return Action(R.string.player_action_remove) {
        removeDialogOpened.value = true
    }
}

@Composable
private fun ChangeOrderMenu(
    expanded: MutableState<Boolean>,
    playersCount: State<Int>,
    onOrderChange: (Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        repeat(playersCount.value) {
            DropdownMenuItem(
                text = { Text("${it + 1}") },
                onClick = {
                    expanded.value = false
                    onOrderChange(it)
                }
            )
        }
    }
}

@Composable
private fun RenameDialog(
    opened: MutableState<Boolean>,
    oldName: String,
    rename: (String) -> Unit
) {
    if (opened.value) {
        val dismiss = { opened.value = false }
        val text = rememberSaveable { mutableStateOf(oldName) }
        val newName = text.value.playerName
        val confirmable = newName != oldName && newName.isValidPlayerName
        AlertDialog(
            onDismissRequest = dismiss,
            properties = DialogProperties(
                dismissOnClickOutside = false
            ),
            confirmButton = {
                Button(
                    enabled = confirmable,
                    onClick = {
                        rename(newName)
                        opened.value = false
                    }
                ) {
                    Text(stringResource(R.string.player_action_rename_confirm))
                }
            },
            dismissButton = {
                Button(
                    onClick = dismiss
                ) {
                    Text(stringResource(R.string.player_action_rename_dismiss))
                }
            },
            text = {
                TextField(
                    value = text.value,
                    onValueChange = { text.value = it }
                )
            }
        )
    }
}

@Composable
private fun RemoveDialog(
    opened: MutableState<Boolean>,
    name: String,
    remove: () -> Unit
) {
    if (opened.value) {
        AlertDialog(
            onDismissRequest = { opened.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        remove()
                        opened.value = false
                    }
                ) {
                    Text(stringResource(R.string.game_player_remove_confirm))
                }
            },
            text = {
                Text(
                    text = stringResource(R.string.game_player_remove_msg) + " $name ?"
                )
            }
        )
    }
}

@Composable
private fun PlayerSettingCardPreview(
    frozen: Boolean,
    selected: Boolean
) {
    BoardGameAssistantTheme {
        PlayerSettingCard(
            id = 1,
            name = "Hello",
            remote = true,
            frozen = frozen,
            selected = selected,
            settingActions = PlayerSettingActions.Empty,
            index = 6,
            playersCount = rememberUpdatedState(10),
            modifier = Modifier.width(250.dp)
        )
    }
}

@Preview
@Composable
private fun PlayerSettingCardPreviewSelected() {
    PlayerSettingCardPreview(
        frozen = false,
        selected = true
    )
}

@Preview
@Composable
private fun PlayerSettingCardPreviewActive() {
    PlayerSettingCardPreview(
        frozen = false,
        selected = false
    )
}

@Preview
@Composable
private fun PlayerSettingCardPreviewFrozen() {
    PlayerSettingCardPreview(
        frozen = true,
        selected = true
    )
}
