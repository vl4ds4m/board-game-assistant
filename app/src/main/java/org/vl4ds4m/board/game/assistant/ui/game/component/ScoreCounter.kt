package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun StandardCounter(
    addPoints: (Int) -> Unit,
    applyEnabled: State<Boolean>?,
    selectNextPlayer: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    ScoreCounter(
        addPoints = addPoints,
        applyEnabled = applyEnabled,
        pointsVariants = listOf(1, 5, 10),
        selectNextPlayer = selectNextPlayer,
        modifier = modifier
    )
}

@Composable
fun ScoreCounter(
    addPoints: (Int) -> Unit,
    applyEnabled: State<Boolean>?,
    selectNextPlayer: (() -> Unit)?,
    pointsVariants: List<Int>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val score = rememberSaveable { mutableIntStateOf(0) }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { score.intValue = 0 },
            ) {
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
            Spacer(Modifier.size(16.dp))
            TextField(
                value = score.intValue
                    .takeIf { it != 0 }
                    ?.toString()
                    ?: "",
                onValueChange = {
                    score.intValue = it.toIntOrNull() ?: 0
                },
                modifier = Modifier.widthIn(min = 120.dp),
                singleLine = true,
                label = { Text("score(s)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(Modifier.size(24.dp))
            Button(
                onClick = {
                    addPoints(score.intValue)
                },
                enabled = applyEnabled?.value ?: true,
                modifier = Modifier.width(90.dp)
            ) {
                Text("Apply")
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PointsAppender(
                pointsVariants = pointsVariants,
                score = score
            )
        }
        selectNextPlayer?.let {
            OutlinedButton(
                onClick = selectNextPlayer,
                modifier = Modifier.width(180.dp)
            ) {
                Text("Next player")
            }
        }
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

@Preview
@Composable
private fun ScoreCounterPreview() {
    BoardGameAssistantTheme {
        ScoreCounter(
            addPoints = {},
            applyEnabled = null,
            selectNextPlayer = {},
            pointsVariants = listOf(1, 2, 120)
        )
    }
}
