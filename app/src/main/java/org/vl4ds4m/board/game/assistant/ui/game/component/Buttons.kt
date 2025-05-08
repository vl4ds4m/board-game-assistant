package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R

@Composable
fun ScoreField(
    score: Score,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.game_master_points_label)
) {
    TextField(
        value = score.text,
        onValueChange = { score.text = it },
        modifier = modifier.width(120.dp),
        singleLine = true,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}

@Stable
class Score {
    private val mText = mutableStateOf("")

    var text: String get() = mText.value
        set(value) {
            value.trim().let {
                if (it.length <= 9) mText.value = it
            }
        }

    val points: Int get() = textToInt ?: 0

    private val textToInt: Int? get() = text.run {
        if (isBlank()) 0
        else toIntOrNull()
    }

    operator fun plusAssign(other: Int) {
        textToInt?.let {
            text = "${it + other}"
        }
    }

    companion object {
        val Saver = Saver<Score, String>(
            save = { it.text },
            restore = { Score().apply { text = it } }
        )
    }
}

@Composable
fun ApplyButton(
    enabled: State<Boolean>?,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled?.value ?: true,
        contentPadding = PaddingValues(6.dp),
        modifier = Modifier.width(75.dp)
    ) {
        Text(
            text = stringResource(R.string.game_master_apply),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

@Composable
fun ResetButton(score: Score) {
    IconButton(
        onClick = { score.text = "" }
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
}

@Composable
fun PointsAppender(
    pointsVariants: List<Int>,
    score: Score,
    labels: List<String>? = null
): Unit = pointsVariants.forEachIndexed { i, points ->
    Button(
        onClick = { score += points },
        contentPadding = PaddingValues(6.dp)
    ) {
        Text(labels?.getOrNull(i) ?: "+ $points")
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
        Text(stringResource(R.string.game_master_next_player))
    }
}