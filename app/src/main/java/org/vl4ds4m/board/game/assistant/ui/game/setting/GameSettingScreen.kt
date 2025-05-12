package org.vl4ds4m.board.game.assistant.ui.game.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.gameName
import org.vl4ds4m.board.game.assistant.ui.game.component.isValidGameName
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme
import java.util.Locale

@Composable
fun GameSettingScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>()
    GameSettingScreenContent(
        name = viewModel.name.collectAsState(),
        onNameChange = { viewModel.name.value = it },
        timeout = viewModel.timeout.collectAsState(),
        onTimeoutChange = { viewModel.timeout.value = it },
        secondsToEnd = viewModel.secondsToEnd.collectAsState(),
        onSecondsToEndChange = { viewModel.changeSecondsToEnd(it) },
        modifier = modifier
    )
}

@Composable
fun GameSettingScreenContent(
    name: State<String>,
    onNameChange: (String) -> Unit,
    timeout: State<Boolean>,
    onTimeoutChange: (Boolean) -> Unit,
    secondsToEnd: State<Int>,
    onSecondsToEndChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val stateChanged = rememberSaveable { mutableIntStateOf(0) }
    val newName = rememberSaveable(name.value, stateChanged.intValue) {
        mutableStateOf(name.value)
    }
    val newTimeout = rememberSaveable(timeout.value, stateChanged.intValue) {
        mutableStateOf(timeout.value)
    }
    val newSeconds = rememberSaveable(secondsToEnd.value, stateChanged.intValue) {
        mutableStateOf(secondsToEnd.value.seconds)
    }
    val newMinutes = rememberSaveable(secondsToEnd.value, stateChanged.intValue) {
        mutableStateOf(secondsToEnd.value.minutes)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.game_settings_title),
            modifier = Modifier.padding(start = 8.dp)
        )
        HorizontalDivider()
        TextField(
            value = newName.value,
            onValueChange = { newName.value = it },
            label = { Text(stringResource(R.string.new_game_name)) },
            singleLine = true,
            modifier = Modifier
                .padding(start = 8.dp)
                .width(240.dp)
        )
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(stringResource(R.string.game_timer_enabled))
                Checkbox(
                    checked = newTimeout.value,
                    onCheckedChange = { newTimeout.value = it }
                )
            }
            Row {
                val width = Modifier.width(90.dp)
                OutlinedTextField(
                    value = newMinutes.value,
                    onValueChange = { value ->
                        value.trim().let {
                            if (it.minutesOrNull != null) {
                                newMinutes.value = it
                            }
                        }
                    },
                    suffix = { Text("m") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.then(width)
                )
                Spacer(Modifier.width(4.dp))
                OutlinedTextField(
                    value = newSeconds.value,
                    onValueChange = { value ->
                        value.trim().let {
                            if (it.secondsOrNull != null) {
                                newSeconds.value = it
                            }
                        }
                    },
                    suffix = { Text("s") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.then(width)
                )
            }
        }
        Button(
            enabled = newName.value.isValidGameName,
            onClick = {
                onNameChange(newName.value.gameName)
                onTimeoutChange(newTimeout.value)
                val validTime = run t@{
                    var sec = newSeconds.value.secondsOrNull
                        ?: return@t false
                    sec += newMinutes.value.minutesOrNull
                        ?.let { it * 60 } ?: return@t false
                    onSecondsToEndChange(sec)
                    return@t sec != 0
                }
                if (!validTime) {
                    onTimeoutChange(false)
                }
                stateChanged.intValue += 1
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.game_settings_apply))
        }
    }
}

@Preview
@Composable
private fun GameSettingScreenPreview() {
    BoardGameAssistantTheme {
        GameSettingScreenContent(
            name = rememberUpdatedState("Some Game"),
            onNameChange = {},
            timeout = rememberUpdatedState(false),
            onTimeoutChange = {},
            secondsToEnd = rememberUpdatedState(12354),
            onSecondsToEndChange = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

private val Int.seconds: String get() = if (this < 0) {
    "00"
} else {
    String.format(Locale.getDefault(), "%02d", this % 60)
}

private val Int.minutes: String get() = if (this < 0) {
    "0"
} else {
    "${this / 60}"
}

private val String.secondsOrNull: Int? get() = trim().run {
    if (isEmpty()) 0
    else toIntOrNull()
        ?.takeIf { it in 0 ..< 60 }
}

private val String.minutesOrNull: Int? get() = trim().run {
    if (isEmpty()) 0
    else toIntOrNull()
        ?.takeIf { it in 0..<1000 }
}
