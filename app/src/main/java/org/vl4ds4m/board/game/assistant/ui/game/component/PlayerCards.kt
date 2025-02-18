package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayerCard(
    playerName: String,
    modifier: Modifier = Modifier,
    onPlayerNameEdited: (String) -> Unit = {},
    onCloseClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(24.dp))
            TextField(
                value = playerName,
                onValueChange = onPlayerNameEdited,
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.titleMedium,
                singleLine = true,
            )
            Spacer(Modifier.width(40.dp))
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clickable(onClick = onCloseClick)
            )
        }
    }
}

@Preview
@Composable
private fun PlayerCardPreview() {
    BoardGameAssistantTheme {
        PlayerCard("Player")
    }
}

@Composable
fun PlayerInGameCard(
    rating: Int,
    name: String,
    score: Int,
    selected: Boolean,
    onSelect: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .clickable(
                enabled = onSelect != null,
                onClick = onSelect ?: {}
            ),
        colors = CardDefaults.cardColors(
            containerColor = with(MaterialTheme.colorScheme) {
                if (selected) surfaceContainerHigh
                else surfaceContainerLow
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$rating. ",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Player image",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(24.dp))
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.width(24.dp))
            Text(
                text = "$score point(s)",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun SimplePlayerInGameCardPreview() {
    PlayerInGameCardPreview(false)
}

@Preview
@Composable
private fun CurrentPlayerInGameCardPreview() {
    PlayerInGameCardPreview(true)
}

@Composable
private fun PlayerInGameCardPreview(current: Boolean) {
    BoardGameAssistantTheme {
        PlayerInGameCard(
            rating = 1,
            name = "Fedya",
            score = 1234,
            selected = current,
            onSelect = null
        )
    }
}
