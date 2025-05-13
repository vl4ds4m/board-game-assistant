package org.vl4ds4m.board.game.assistant.ui.game.end

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.Users
import org.vl4ds4m.board.game.assistant.ui.detailedGameSessionPreview
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.PlayerStats
import org.vl4ds4m.board.game.assistant.ui.game.component.PlayersRating
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

/**
 * Displays a game end screen with players results.
 */
@Composable
fun EndGameScreen(
    navigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel<GameViewModel>()
    EndGameScreenContent(
        players = viewModel.players.collectAsState(),
        users = viewModel.users.collectAsState(),
        playerStats = viewModel.gameUi.playerStats,
        navigateHome = navigateHome,
        modifier = modifier
    )
}

@Composable
fun EndGameScreenContent(
    players: State<Players>,
    users: State<Users>,
    playerStats: PlayerStats,
    navigateHome: () -> Unit,
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
            Button(
                onClick = navigateHome
            ) {
                Text(
                    text = stringResource(R.string.game_end_to_play)
                )
            }
        }
        HorizontalDivider()
        PlayersRating(
            players = players,
            users = users,
            currentPid = rememberUpdatedState(null),
            onSelectPlayer = null,
            playerStats = playerStats,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
        Spacer(Modifier.size(48.dp))
    }

}

@Preview
@Composable
private fun EndGameScreenPreview() {
    BoardGameAssistantTheme {
        with(detailedGameSessionPreview) {
            EndGameScreenContent(
                players = rememberUpdatedState(players.toMap()),
                users = rememberUpdatedState(users),
                playerStats = GameUI.playerStats,
                navigateHome = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
