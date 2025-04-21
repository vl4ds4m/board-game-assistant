package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import org.vl4ds4m.board.game.assistant.game.Dice
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun NewGameStartScreen(
    viewModel: GameSetupViewModel,
    onSetupPlayers: (GameType) -> Unit,
    modifier: Modifier = Modifier,
) {
    NewGameStartScreenContent(
        vmType = viewModel.type,
        vmName = viewModel.name,
        onSetupPlayers = {
            viewModel.createGame(it)
            onSetupPlayers(it)
        },
        modifier = modifier
    )
}

@Composable
fun NewGameStartScreenContent(
    vmType: MutableState<GameType?>,
    vmName: MutableState<String>,
    onSetupPlayers: (GameType) -> Unit,
    modifier: Modifier = Modifier
) {
    val (type, onTypeChanged) = vmType
    val (name, onNameChanged) = vmName
    Column(
        modifier = modifier.padding(64.dp),
        verticalArrangement = Arrangement.spacedBy(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = onNameChanged,
            label = { Text(stringResource(R.string.new_game_name)) },
            singleLine = true
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GameType.entries.forEach {
                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .clickable {
                            onTypeChanged(it)
                        },
                    colors = if (type == it) {
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    } else {
                        CardDefaults.cardColors()
                    }
                ) {
                    Text(
                        text = stringResource(it.localizedStringId),
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .fillMaxHeight()
                            .wrapContentHeight()
                    )
                }
            }
        }
        Button(
            onClick = {
                type?.let(onSetupPlayers)
            },
            enabled = name.isNotBlank() && type != null
        ) {
            Text(stringResource(R.string.new_game_next_step))
        }
    }
}

@Preview
@Composable
private fun NewGameStartScreenPreview() {
    BoardGameAssistantTheme {
        NewGameStartScreenContent(
            vmType = remember { mutableStateOf(Dice) },
            vmName = remember { mutableStateOf("My game") },
            onSetupPlayers = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
