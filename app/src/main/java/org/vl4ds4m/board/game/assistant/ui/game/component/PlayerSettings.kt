package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.component.isValidPlayerName
import org.vl4ds4m.board.game.assistant.ui.component.playerName

@Composable
fun PlayersHead(
    modifier: Modifier = Modifier,
    addPlayer: (String) -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.game_players_list),
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        val playerPrefix = stringResource(R.string.game_player_prefix)
        FloatingActionButton(
            onClick = {
                addPlayer(playerPrefix)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add a player"
            )
        }
    }
}

@Composable
fun NoPlayersLabel(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.new_game_players_empty_list),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
    )
}

@Composable
fun RemotePlayersHead() {
    Text(
        text = stringResource(R.string.game_online_players),
        modifier = Modifier.padding(start = 16.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun NoRemotePlayersLabel(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.game_online_players_empty),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
    )
}

@Composable
fun PlayerSettingButton(
    modifier: Modifier = Modifier,
    actionsMenu: @Composable () -> List<PlayerMenuItem>
) {
    val expanded = remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded.value = true },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Player setting",
            modifier = Modifier.size(24.dp)
        )
        val menu = actionsMenu()
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            menu.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(item.text())
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

data class PlayerMenuItem(
    val text: @Composable () -> String,
    val action: () -> Unit
) {
    constructor(@StringRes resId: Int, action: () -> Unit) : this(
        text = { stringResource(resId) },
        action = action
    )
}

@Composable
fun onRenameAction(name: String, onRename: (String) -> Unit): PlayerMenuItem {
    val renameExp = remember { mutableStateOf(false) }
    RenameDialog(
        opened = renameExp,
        oldName = name,
        rename = onRename
    )
    return PlayerMenuItem(R.string.player_action_rename) {
        renameExp.value = true
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
fun onOrderChangeAction(
    playersCount: State<Int>,
    onOrderChange: (Int) -> Unit
): PlayerMenuItem {
    val expanded = remember { mutableStateOf(false) }
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
    return PlayerMenuItem(R.string.player_action_change_order) {
        expanded.value = true
    }
}
