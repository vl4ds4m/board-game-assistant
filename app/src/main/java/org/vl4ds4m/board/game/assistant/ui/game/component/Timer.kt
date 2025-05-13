package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.prettyTime

/**
 * Displays a time until game ending.
 */
@Composable
fun Timer(
    state: State<Int?>,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        val seconds = state.value ?: 0
        Text(
            text = stringResource(R.string.game_timer_prefix)
                + ": " + prettyTime(seconds),
            color = if (seconds <= 5) {
                MaterialTheme.colorScheme.error
            } else {
                Color.Unspecified
            },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
        )
    }
}
