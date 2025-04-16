package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ScoreField(
    score: MutableIntState,
    modifier: Modifier = Modifier,
    label: String = "score(s)"
) {
    TextField(
        value = score.intValue
            .takeIf { it != 0 }
            ?.toString()
            ?: "",
        onValueChange = {
            score.intValue = it.toIntOrNull() ?: 0
        },
        modifier = modifier.widthIn(min = 120.dp),
        singleLine = true,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun ApplyButton(
    enabled: State<Boolean>?,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled?.value ?: true,
        modifier = Modifier.width(90.dp)
    ) {
        Text("Apply")
    }
}

@Composable
fun ResetButton(onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Reset",
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(8.dp)
        )
    }
}

@Composable
fun PointsAppender(
    pointsVariants: List<Int>,
    score: MutableIntState
): Unit = pointsVariants.forEach { points ->
    Button(
        onClick = { score.intValue += points }
    ) {
        Text("+ $points")
    }
}

@Composable
fun NextPlayerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.width(180.dp)
    ) {
        Text("Next player")
    }
}