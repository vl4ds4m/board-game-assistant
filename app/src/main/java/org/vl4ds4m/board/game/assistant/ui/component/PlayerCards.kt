package org.vl4ds4m.board.game.assistant.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R

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
fun PlayerName(name: String, user: Boolean, modifier: Modifier = Modifier) {
    val text = name + if (user) {
        " (" + stringResource(R.string.game_player_self_label) + ")"
    } else {
        ""
    }
    Text(
        text = text,
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
    if (remote || frozen) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (remote) RemoteIcon()
            if (frozen) FrozenIcon()
        }
    }
}

@Composable
fun RemoteIcon() {
    Icon(
        imageVector = Icons.Filled.Place,
        contentDescription = "Remote",
        modifier = Modifier.size(16.dp)
    )
}

@Composable
fun FrozenIcon() {
    Icon(
        painter = painterResource(R.drawable.frozen_24px),
        contentDescription = "Frozen",
        modifier = Modifier.size(16.dp)
    )
}

val String.playerName: String get() = trim()

val String.isValidPlayerName: Boolean get() = playerName.run {
    length in 3 .. 20
}
