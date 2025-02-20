package org.vl4ds4m.board.game.assistant.ui.game.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun NewGameStartScreen(
    viewModel: GameSetupViewModel,
    onBackClick: () -> Unit,
    onSetupPlayers: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GameScreen(
        topBarTitle = "New game",
        onBackClick = onBackClick,
        modifier = modifier
    ) { innerModifier ->
        NewGameStartScreenContent(
            vmType = viewModel.type,
            vmName = viewModel.name,
            onSetupPlayers = onSetupPlayers,
            modifier = innerModifier
        )
    }
}

@Composable
fun NewGameStartScreenContent(
    vmType: MutableState<GameType?>,
    vmName: MutableState<String>,
    onSetupPlayers: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (type, onTypeChanged) = vmType
    val (name, onNameChanged) = vmName
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.padding(48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = onNameChanged,
            label = { Text("Name") },
            singleLine = true
        )
        Spacer(Modifier.height(48.dp))
        FilledTonalButton({ expanded = true }) {
            Text(type?.title ?: "Select game")
            DropdownMenu(expanded, { expanded = false }) {
                for (entry in GameType.entries) {
                    DropdownMenuItem(
                        text = { Text(entry.title) },
                        onClick = { onTypeChanged(entry); expanded = false }
                    )
                }
            }
        }
        Spacer(Modifier.height(48.dp))
        Button(
            onClick = onSetupPlayers,
            enabled = name.isNotBlank() && type != null
        ) {
            Text("Continue")
        }
    }
}

@Preview
@Composable
private fun NewGameStartScreenPreview() {
    BoardGameAssistantTheme {
        NewGameStartScreenContent(
            vmType = remember { mutableStateOf(null) },
            vmName = remember { mutableStateOf("") },
            onSetupPlayers = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
