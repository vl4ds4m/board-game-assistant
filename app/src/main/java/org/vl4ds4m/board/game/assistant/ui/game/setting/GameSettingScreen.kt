package org.vl4ds4m.board.game.assistant.ui.game.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.map
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun GameSettingScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>()
    GameSettingScreenContent(
        name = viewModel.name.collectAsState(""),
        onNameChange = { viewModel.name.value = it },
        timeout = viewModel.timeout.collectAsState(),
        onTimeoutChange = { viewModel.timeout.value = it },
        secondsToEnd = viewModel.secondsToEnd.map {
            if (it <= 0) ""
            else it.toString()
        }.collectAsState(""),
        onSecondsToEndChange = { text ->
            val seconds = text.toIntOrNull() ?: 0
            viewModel.changeSecondsToEnd(seconds)
        },
        modifier = modifier
    )
}

@Composable
fun GameSettingScreenContent(
    name: State<String>,
    onNameChange: (String) -> Unit,
    timeout: State<Boolean>,
    onTimeoutChange: (Boolean) -> Unit,
    secondsToEnd: State<String>,
    onSecondsToEndChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Text(stringResource(R.string.game_settings_title))
        HorizontalDivider()
        TextField(
            value = name.value,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.new_game_name)) }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.game_timer_enabled))
            Checkbox(
                checked = timeout.value,
                onCheckedChange = onTimeoutChange
            )
        }
        TextField(
            value = secondsToEnd.value,
            onValueChange = onSecondsToEndChange,
            label = { Text(stringResource(R.string.game_timer_seconds_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}

@Preview
@Composable
private fun GameSettingScreenPreview() {
    BoardGameAssistantTheme {
        GameSettingScreenContent(
            name = remember { mutableStateOf("Some Game") },
            onNameChange = {},
            timeout = remember { mutableStateOf(false) },
            onTimeoutChange = {},
            secondsToEnd = remember { mutableStateOf("") },
            onSecondsToEndChange = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
