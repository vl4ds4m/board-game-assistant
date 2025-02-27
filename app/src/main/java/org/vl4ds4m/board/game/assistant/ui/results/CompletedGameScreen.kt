package org.vl4ds4m.board.game.assistant.ui.results

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.data.Score
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun CompletedGameScreen(
    name: String,
    players: Map<Long, Player>,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    GameScreen(
        topBarTitle = name,
        onBackClick = navigateUp,
        modifier = modifier
    ) { innerModifier ->
        PlayersRating(
            players = remember { mutableStateOf(players) },
            currentPlayerId = remember { mutableStateOf(null) },
            onSelectPlayer = null,
            modifier = innerModifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun CompletedGameScreenPreview() {
    BoardGameAssistantTheme {
        CompletedGameScreen(
            name = "Some game",
            players = mapOf(
                1L to Player(
                    name = "Abv",
                    active = true,
                    state = Score(45)
                ),
                2L to Player(
                    name = "Efo",
                    active = false,
                    state = Score(123)
                ),
                3L to Player(
                    name = "Urt",
                    active = true,
                    state = Score(59)
                )
            ),
            navigateUp = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
