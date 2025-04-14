package org.vl4ds4m.board.game.assistant.ui.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun GameSessionCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(50.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight()
                .padding(horizontal = 20.dp),
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun GameSessionCardPreview() {
    BoardGameAssistantTheme {
        GameSessionCard("Some game", Modifier.width(200.dp))
    }
}
