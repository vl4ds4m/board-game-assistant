package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.data.Player
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
internal fun PlayerCard(player: Player, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
    ) {
        val betweenPadding = 24.dp
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = betweenPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = player.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = betweenPadding),
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PlayerCardPreview() {
    BoardGameAssistantTheme {
        PlayerCard(Player(0, "Abc"))
    }
}
