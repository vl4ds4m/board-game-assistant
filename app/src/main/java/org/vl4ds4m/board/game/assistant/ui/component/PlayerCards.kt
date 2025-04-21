package org.vl4ds4m.board.game.assistant.ui.component

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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun NewPlayerCard(
    name: String,
    remote: Boolean,
    editName: (String) -> Unit,
    remove: () -> Unit,
    moveUp: (() -> Unit)?,
    moveDown: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Image",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(24.dp))
            TextField(
                value = name,
                onValueChange = editName,
                modifier = Modifier.weight(1f),
                readOnly = remote,
                textStyle = MaterialTheme.typography.titleMedium,
                singleLine = true,
            )
            if (remote) {
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Remote",
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(40.dp))
            val expanded = remember { mutableStateOf(false) }
            IconButton(
                onClick = { expanded.value = true },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Actions",
                )
                ActionMenu(
                    expanded = expanded,
                    actions = buildList {
                        add(PlayerAction(
                            name = stringResource(R.string.player_action_remove),
                            action = remove
                        ))
                        moveUp?.let {
                            add(PlayerAction(
                                name = stringResource(R.string.player_action_move_up),
                                action = it
                            ))
                        }
                        moveDown?.let {
                            add(PlayerAction(
                                name = stringResource(R.string.player_action_move_down),
                                action = it
                            ))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NewRemotePlayerCard(
    name: String,
    add: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .clickable { add() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Image",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(24.dp))
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

private data class PlayerAction(
    val name: String,
    val action: () -> Unit
)

@Composable
private fun ActionMenu(
    expanded: MutableState<Boolean>,
    actions: List<PlayerAction>
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
    ) {
        actions.forEach { (name, action) ->
            DropdownMenuItem(
                text = { Text(name) },
                onClick = {
                    expanded.value = false
                    action()
                }
            )
        }
    }
}

@Preview
@Composable
private fun NewPlayerCardPreview() {
    BoardGameAssistantTheme {
        NewPlayerCard(
            name = "Player",
            remote = true,
            editName = {},
            remove = {},
            moveUp = {},
            moveDown = {},
            modifier = Modifier.width(300.dp)
        )
    }
}

@Preview
@Composable
private fun NewRemotePlayerCardPreview() {
    BoardGameAssistantTheme {
        NewRemotePlayerCard(
            name = "Player",
            add = {},
            modifier = Modifier.width(300.dp)
        )
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
            .height(50.dp)
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
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$rating. ",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Player image",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.width(8.dp))
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
