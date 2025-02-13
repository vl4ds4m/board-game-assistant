package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

@Composable
fun ScoreCounter(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier

) {
    ScoreCounter(
        onPointsAdd = viewModel::addPoints,
        modifier = modifier
    )
}

@Composable
fun ScoreCounter(
    onPointsAdd: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val (score, onScoreChanged) = rememberSaveable { mutableStateOf("") }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = score,
            onValueChange = onScoreChanged,
            modifier = Modifier.width(150.dp),
            singleLine = true,
            suffix = { Text(" score(s)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(Modifier.width(24.dp))
        Button(
            onClick = {
                score.toIntOrNull()?.let(onPointsAdd)
            },
            modifier = Modifier.width(90.dp)
        ) {
            Text("Apply")
        }
    }
}
