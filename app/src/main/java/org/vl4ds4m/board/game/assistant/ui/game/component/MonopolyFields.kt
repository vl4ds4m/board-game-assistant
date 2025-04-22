package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.vl4ds4m.board.game.assistant.game.monopoly.monopolyFields

@Composable
fun MonopolyField(
    position: Int,
    modifier: Modifier = Modifier
) {
    val field = monopolyFields[position]
    Text(
        text = field?.let { stringResource(field.resId) } ?: "???",
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}
