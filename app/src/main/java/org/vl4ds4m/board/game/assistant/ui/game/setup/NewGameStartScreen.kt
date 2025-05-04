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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.Dice
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.ui.game.component.gameName
import org.vl4ds4m.board.game.assistant.ui.game.component.isValidGameName
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun NewGameStartScreen(
    viewModel: GameSetupViewModel,
    onSetupPlayers: (GameType) -> Unit,
    modifier: Modifier = Modifier,
) {
    NewGameStartScreenContent(
        type = viewModel.type.collectAsState(),
        onTypeChanged = { viewModel.type.value = it },
        defaultNameNum = viewModel.defaultNameNum.collectAsState(0),
        onSetupPlayers = { type, name ->
            viewModel.createGame(type, name)
            onSetupPlayers(type)
        },
        modifier = modifier
    )
}

@Composable
fun NewGameStartScreenContent(
    type: State<GameType?>,
    onTypeChanged: (GameType) -> Unit,
    defaultNameNum: State<Int>,
    onSetupPlayers: (GameType, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(64.dp),
        verticalArrangement = Arrangement.spacedBy(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val name = rememberSaveable { mutableStateOf("") }
        val defaultName = name.value.isEmpty() && type.value != null
        val finalName = if (defaultName) {
            type.value?.let {
                stringResource(it.localizedStringId) + " #" + defaultNameNum.value
            } ?: "New game"
        } else {
            name.value
        }
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = {
                if (defaultName) {
                    Text(finalName)
                } else {
                    Text(stringResource(R.string.new_game_name))
                }
            },
            singleLine = true
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GameType.entries.forEach {
                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .clip(CardDefaults.shape)
                        .fillMaxWidth()
                        .clickable {
                            onTypeChanged(it)
                        },
                    colors = if (type.value == it) {
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
                type.value?.let {
                    onSetupPlayers(it, finalName.gameName)
                }
            },
            enabled = type.value != null && finalName.isValidGameName
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
            type = rememberUpdatedState(Dice),
            onTypeChanged = {},
            defaultNameNum = rememberUpdatedState(4),
            onSetupPlayers = { _, _ -> },
            modifier = Modifier.fillMaxSize()
        )
    }
}
