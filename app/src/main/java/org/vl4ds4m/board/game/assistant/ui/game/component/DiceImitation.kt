package org.vl4ds4m.board.game.assistant.ui.game.component

import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Displays some game dice and buttons to act with it.
 */
@Composable
fun DiceImitationScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        val dice = rememberSaveable(
            saver = listSaver<MutableList<Int>, Int>(
                save = { it },
                restore = { it.toMutableStateList() }
            )
        ) {
            mutableStateListOf(1)
        }
        val addEnabled = remember {
            derivedStateOf { dice.size < 9 }
        }
        val removeEnabled = remember {
            derivedStateOf { dice.size > 1 }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {
                    repeat(dice.size) { i ->
                        dice[i] = Random.nextInt(1..6)
                    }
                },
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(stringResource(R.string.dice_imitation_roll_dice))
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                enabled = addEnabled.value,
                onClick = { dice.add(dice[0]) }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add new dice"
                )
            }
            IconButton(
                enabled = removeEnabled.value,
                onClick = { dice.removeAt(dice.lastIndex) }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove last dice"
                )
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(dice) { index, value ->
                Text(
                    text = stringResource(R.string.dice_imitation_dice_prefix) + " ${index + 1}:     $value",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        val clipboard = LocalContext.current.getSystemService(ClipboardManager::class.java)
        TextButton(
            onClick = {
                dice.sum().toString().let {
                    ClipData.newPlainText("Dice summary", it)
                }.let {
                    clipboard.setPrimaryClip(it)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(stringResource(R.string.dice_imitation_copy_sum))
        }
        Spacer(Modifier.weight(0.3f))
    }
}

@Preview
@Composable
private fun DiceImitationScreenPreview() {
    BoardGameAssistantTheme {
        DiceImitationScreen(Modifier.fillMaxSize())
    }
}
