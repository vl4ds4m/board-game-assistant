package org.vl4ds4m.board.game.assistant.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun PlayerCard(
    selected: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    content: @Composable RowScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(shape),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = with(MaterialTheme.colorScheme) {
                if (selected) surfaceContainerHigh
                else surfaceContainerLow
            }
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            content = content
        )
    }
}

@Composable
fun PlayerPosition(position: Int, modifier: Modifier = Modifier) {
    val validPos = position in 1 .. 99
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.width(25.dp)
    ) {
        Text(
            text = if (validPos) {
                "$position."
            } else {
                "99"
            },
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
        if (!validPos) {
            Text(
                text = "+",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(6.dp)
            )
        }
    }
}

@Composable
fun PlayerIcon(playerName: String, modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "$playerName's image",
        modifier = modifier.size(40.dp)
    )
}

@Composable
fun PlayerName(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1
    )
}

@Composable
fun PlayerState(
    topRow: @Composable RowScope.() -> Unit,
    bottomRow: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyMedium
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                content = topRow
            )
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                content = bottomRow
            )
        }
    }
}

@Composable
fun PlayerIndicators(
    remote: Boolean,
    frozen: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (remote) RemoteIcon()
        if (frozen) FrozenIcon()
    }
}

@Composable
private fun RemoteIcon() {
    Icon(
        imageVector = Icons.Filled.Place,
        contentDescription = "Remote",
        modifier = Modifier.size(16.dp)
    )
}

@Composable
private fun FrozenIcon() {
    Icon(
        painter = painterResource(R.drawable.frozen_24px),
        contentDescription = "Frozen",
        modifier = Modifier.size(16.dp)
    )
}

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
    bind: ((Long) -> Unit)?,
    bindList: State<List<Pair<Long, String>>>?,
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
            Spacer(Modifier.width(40.dp))
            val expanded = remember { mutableStateOf(false) }
            if (bind != null && bindList != null) {
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
                            bindList.value.forEach { (id, name) ->
                                add(PlayerAction(
                                    name = stringResource(R.string.player_action_bind) + " $name",
                                    action = { bind(id) }
                                ))
                            }
                        }
                    )
                }
            }
        }
    }
}

val String.playerName: String get() = trim()

val String.isValidPlayerName: Boolean get() = playerName.run {
    length in 3 .. 20
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
            bind = {},
            bindList = rememberUpdatedState(listOf(1L to "Oret")),
            modifier = Modifier.width(300.dp)
        )
    }
}
