package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

/**
 * Displays a start screen to setup a new game.
 */
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
        modifier = modifier.padding(
            horizontal = 48.dp,
            vertical = 64.dp
        ),
        verticalArrangement = Arrangement.spacedBy(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val name = rememberSaveable { mutableStateOf("") }
        val defaultName = name.value.isEmpty() && type.value != null
        val finalName = if (defaultName) {
            type.value?.let {
                stringResource(it.nameResId) + " #" + defaultNameNum.value
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
            singleLine = true,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GameType.entries.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(40.dp))
                    GameCard(
                        text = stringResource(it.nameResId),
                        selected = type.value == it,
                        onClick = { onTypeChanged(it) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Description(
                        prompt = stringResource(it.nameResId),
                        content = stringResource(it.descResId)
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

@Composable
private fun GameCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(40.dp)
            .clip(CardDefaults.shape)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = if (selected) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxHeight()
                .wrapContentHeight()
        )
    }
}

@Composable
private fun Description(prompt: String, content: String) {
    val opened = remember { mutableStateOf(false) }
    IconButton(
        onClick = { opened.value = true },
        modifier = Modifier.size(32.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = prompt
        )
    }
    if (!opened.value) return
    AlertDialog(
        onDismissRequest = { opened.value = false },
        confirmButton = {
            Button(
                onClick = { opened.value = false }
            ) {
                Text(stringResource(R.string.game_description_confirm))
            }
        },
        text = { Text(content) }
    )
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
