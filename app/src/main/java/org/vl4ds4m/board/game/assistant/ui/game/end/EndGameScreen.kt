package org.vl4ds4m.board.game.assistant.ui.game.end

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun EndGameScreen(
    onHomeNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = 48.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.game_end_msg),
            style = MaterialTheme.typography.headlineLarge
        )
        Button(
            onClick = onHomeNavigate
        ) {
            Text(
                text = stringResource(R.string.game_end_to_play)
            )
        }
    }
}

@Preview
@Composable
private fun EndGameScreenPreview() {
    BoardGameAssistantTheme {
        EndGameScreen(
            onHomeNavigate = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
