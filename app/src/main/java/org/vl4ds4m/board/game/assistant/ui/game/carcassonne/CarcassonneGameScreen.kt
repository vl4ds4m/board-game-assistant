package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.domain.game.carcassonne.CarcassonneProperty
import org.vl4ds4m.board.game.assistant.ui.game.GameScreenContent
import org.vl4ds4m.board.game.assistant.ui.game.OrderedGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.fakePlayers
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme
import org.vl4ds4m.board.game.assistant.util.title

@Composable
fun CarcassonneGameScreen(
    viewModel: CarcassonneGameViewModel,
    onGameComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OrderedGameScreen(
        viewModel = viewModel,
        onGameComplete = onGameComplete,
        masterActions = {
            CarcassonneCounter(
                onAddPoints = { viewModel.addPoints(it) }
            )
        },
        modifier = modifier
    )
}

@Composable
fun CarcassonneCounter(
    onAddPoints: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val count = rememberSaveable { mutableStateOf<Int?>(null) }
    var property by rememberSaveable {
        mutableStateOf<CarcassonneProperty?>(null)
    }
    val skippable = remember {
        derivedStateOf {
            val empty = count.value?.let { it <= 0 } ?: true
            empty || property == null
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var expanded by remember { mutableStateOf(false) }
            FilledTonalButton(
                onClick = { expanded = true }
            ) {
                Text(
                    text = property?.title ?: "Property"
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        property = null
                        expanded = false
                    }
                ) {
                    for (entry in CarcassonneProperty.entries) {
                        DropdownMenuItem(
                            text = { Text(entry.title) },
                            onClick = {
                                property = entry
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.width(24.dp))
            TextField(
                value = count.value?.toString() ?: "",
                onValueChange = { text ->
                    if (text.isBlank()) {
                        count.value = null
                    } else {
                        text.toIntOrNull()?.takeIf {
                            it >= 0
                        }?.let {
                            count.value = it
                        }
                    }
                },
                modifier = Modifier.width(200.dp),
                singleLine = true,
                placeholder = { Text("Count") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
        Spacer(Modifier.height(30.dp))
        Button(
            onClick = {
                if (skippable.value) {
                    onAddPoints(0)
                } else {
                    onAddPoints(count.value!!)
                }
            },
            modifier = Modifier.width(90.dp)
        ) {
            Text(
                text = if (skippable.value) {
                    "Skip"
                } else {
                    "Apply"
                }
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun CarcassonneGameScreenPreview() {
    BoardGameAssistantTheme {
        GameScreenContent(
            name = "Carcassonne",
            players = mutableStateOf(fakePlayers),
            currentPlayerId = mutableStateOf(null),
            onSelectPlayer = null,
            masterActions = {
                CarcassonneCounter(
                    onAddPoints = {}
                )
            },
            onGameComplete = {}
        )
    }
}
