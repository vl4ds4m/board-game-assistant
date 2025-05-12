package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.ui.component.PlayerCard
import org.vl4ds4m.board.game.assistant.ui.component.PlayerIcon
import org.vl4ds4m.board.game.assistant.ui.component.PlayerName
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

/**
 * Displays a card of remote player with his name and action button.
 */
@Composable
fun RemotePlayerCard(
    name: String,
    user: Boolean,
    add: () -> Unit,
    bind: ((PID) -> Unit)?,
    bindList: State<List<Pair<PID, String>>>?,
    modifier: Modifier = Modifier
) {
    PlayerCard(
        selected = false,
        modifier = modifier.clickable { add() }
    ) {
        PlayerIcon(name)
        PlayerName(
            name = name,
            user = user,
            modifier = Modifier.weight(1f)
        )
        if (bind != null && bindList != null) {
            PlayerSettingButton {
                bindList.value.map { (id, nm) ->
                    PlayerMenuItem(
                        text = { stringResource(R.string.player_action_bind) + " $nm"},
                        action = { bind(id) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RemotePlayerCardPreview() {
    BoardGameAssistantTheme {
        RemotePlayerCard(
            name = "Player",
            user = true,
            add = {},
            bind = {},
            bindList = rememberUpdatedState(listOf(1 to "Oret")),
            modifier = Modifier.width(300.dp)
        )
    }
}
