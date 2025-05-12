package org.vl4ds4m.board.game.assistant.ui.game.setting

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.ui.component.PlayerCard
import org.vl4ds4m.board.game.assistant.ui.component.PlayerIcon
import org.vl4ds4m.board.game.assistant.ui.component.PlayerIndicators
import org.vl4ds4m.board.game.assistant.ui.component.PlayerName
import org.vl4ds4m.board.game.assistant.ui.component.PlayerPosition
import org.vl4ds4m.board.game.assistant.ui.component.PlayerState
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayerMenuItem
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayerSettingButton
import org.vl4ds4m.board.game.assistant.ui.game.component.onOrderChangeAction
import org.vl4ds4m.board.game.assistant.ui.game.component.onRenameAction
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

/**
 * Displays information about a player on setting screens.
 * Contains his position in ordered list, name, whether it is remote or frozen,
 * button with context actions.
 */
@Composable
fun PlayerSettingCard(
    id: PID,
    name: String,
    user: Boolean,
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
                    user = user,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            bottomRow = {
                PlayerIndicators(
                    remote = remote,
                    frozen = frozen
                )
            },
            modifier = Modifier.weight(1f)
        )
        PlayerSettingButton {
            actionsMenu(
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
}

@Composable
private fun actionsMenu(
    playerId: PID,
    name: String,
    remote: Boolean,
    frozen: Boolean,
    selected: Boolean,
    actions: PlayerSettingActions,
    playersCount: State<Int>
): List<PlayerMenuItem> {
    val menu = mutableListOf<PlayerMenuItem>()
    actions.onOrderChange?.let { onOrderChange ->
        menu += onOrderChangeAction(playersCount) {
            onOrderChange(playerId, it)
        }
    }
    if (!frozen && !selected) {
        menu += PlayerMenuItem(R.string.player_action_select) {
            actions.onSelect(playerId)
        }
    }
    menu += onRenameAction(name) {
        actions.onRename(playerId, it)
    }
    menu += if (frozen) {
        PlayerMenuItem(R.string.player_action_unfreeze) {
            actions.onUnfreeze(playerId)
        }
    } else {
        PlayerMenuItem(R.string.player_action_freeze) {
            actions.onFreeze(playerId)
        }
    }
    if (remote) {
        menu += PlayerMenuItem(R.string.player_action_unbind) {
            actions.onUnbind(playerId)
        }
    }
    menu += onRemoveAction(name) { actions.onRemove(playerId) }
    return menu
}

@Composable
private fun onRemoveAction(name: String, onRemove: () -> Unit): PlayerMenuItem {
    val removeDialogOpened = remember { mutableStateOf(false) }
    RemoveDialog(
        opened = removeDialogOpened,
        name = name,
        remove = onRemove
    )
    return PlayerMenuItem(R.string.player_action_remove) {
        removeDialogOpened.value = true
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
            user = true,
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
