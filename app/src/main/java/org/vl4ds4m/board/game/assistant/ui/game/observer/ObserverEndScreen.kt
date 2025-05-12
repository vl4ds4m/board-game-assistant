package org.vl4ds4m.board.game.assistant.ui.game.observer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.Users
import org.vl4ds4m.board.game.assistant.ui.detailedGameSessionPreview
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

/**
 * Displays information about end of the game.
 */
@Composable
fun ObserverEndScreen(
    players: State<Players>,
    users: State<Users>,
    gameUiFactory: State<GameUI.Factory>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.game_end_msg),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.size(24.dp))
            Text(
                text = stringResource(R.string.observe_end_msg),
                textAlign = TextAlign.Center
            )
        }
        HorizontalDivider()
        PlayersRating(
            players = players,
            users = users,
            currentPid = rememberUpdatedState(null),
            onSelectPlayer = null,
            playerStats = gameUiFactory.value.playerStats,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
        Spacer(Modifier.size(48.dp))
    }
}

@Preview
@Composable
private fun ObserverEndScreenPreview() {
    BoardGameAssistantTheme {
        with (detailedGameSessionPreview) {
            ObserverEndScreen(
                players = rememberUpdatedState(players.toMap()),
                users = rememberUpdatedState(users),
                gameUiFactory = rememberUpdatedState(GameUI)
            )
        }
    }
}