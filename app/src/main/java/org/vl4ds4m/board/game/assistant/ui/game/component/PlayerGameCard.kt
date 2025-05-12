package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.ui.component.PlayerCard
import org.vl4ds4m.board.game.assistant.ui.component.PlayerIcon
import org.vl4ds4m.board.game.assistant.ui.component.PlayerIndicators
import org.vl4ds4m.board.game.assistant.ui.component.PlayerName
import org.vl4ds4m.board.game.assistant.ui.component.PlayerPosition
import org.vl4ds4m.board.game.assistant.ui.component.PlayerState

/**
 * Displays information about a player. Contains a player name,
 * score and another game data, icons on activity and whether
 * the player is remote.
 */
@Composable
fun PlayerGameCard(
    position: Int,
    name: String,
    user: Boolean,
    remote: Boolean,
    frozen: Boolean,
    stats: @Composable RowScope.() -> Unit,
    selected: Boolean,
    onCardSelected: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    PlayerCard(
        selected = selected,
        modifier = modifier.clickable(
            enabled = onCardSelected != null,
            onClick = onCardSelected ?: {}
        )
    ) {
        PlayerPosition(position)
        PlayerIcon(name)
        PlayerState(
            topRow = {
                PlayerName(
                    name = name,
                    user = user,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                PlayerIndicators(
                    remote = remote,
                    frozen = frozen
                )
            },
            bottomRow = stats
        )
    }
}

@Preview
@Composable
private fun PlayerGameCardPreview() {
    PlayerGameCard(
        position = 73,
        name = "Abdildfhmud Treasd lj eli sdf",
        user = true,
        remote = true,
        frozen = true,
        stats = {
            Spacer(Modifier.weight(1f))
            Text("23 634.54 $")
        },
        selected = true,
        onCardSelected = {},
        modifier = Modifier
            .height(60.dp)
            .width(300.dp)
    )
}
