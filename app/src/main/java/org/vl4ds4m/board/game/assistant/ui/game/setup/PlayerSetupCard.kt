package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.component.PlayerCard
import org.vl4ds4m.board.game.assistant.ui.component.PlayerIcon
import org.vl4ds4m.board.game.assistant.ui.component.PlayerName
import org.vl4ds4m.board.game.assistant.ui.component.PlayerPosition
import org.vl4ds4m.board.game.assistant.ui.component.PlayerState
import org.vl4ds4m.board.game.assistant.ui.component.RemoteIcon
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayerMenuItem
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayerSettingButton
import org.vl4ds4m.board.game.assistant.ui.game.component.onOrderChangeAction
import org.vl4ds4m.board.game.assistant.ui.game.component.onRenameAction
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayerSetupCard(
    index: Int,
    name: String,
    remote: Boolean,
    setupActions: PlayerSetupActions,
    playersCount: State<Int>,
    modifier: Modifier = Modifier
) {
    PlayerCard(
        selected = false,
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
                if (remote) RemoteIcon()
            },
            modifier = Modifier.weight(1f)
        )
        PlayerSettingButton {
            listOf(
                onRenameAction(name) {
                    setupActions.onRename(index, it)
                },
                PlayerMenuItem(R.string.player_action_remove) {
                    setupActions.onRemove(index)
                },
                onOrderChangeAction(playersCount) {
                    setupActions.onOrderChange(index, it)
                }
            )
        }
    }
}

@Preview
@Composable
private fun PlayerSetupCardPreview() {
    BoardGameAssistantTheme {
        PlayerSetupCard(
            index = 5,
            name = "Player",
            remote = true,
            setupActions = PlayerSetupActions.Empty,
            playersCount = rememberUpdatedState(12),
            modifier = Modifier.width(300.dp)
        )
    }
}
