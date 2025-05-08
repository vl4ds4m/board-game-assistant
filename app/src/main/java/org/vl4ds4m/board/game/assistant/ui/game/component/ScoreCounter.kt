package org.vl4ds4m.board.game.assistant.ui.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        val score = rememberSaveable(saver = Score.Saver) { Score() }
        val enabled = remember {
            derivedStateOf {
                score.points != 0
                && applyEnabled?.value ?: true
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ResetButton(score)
            Spacer(Modifier.size(8.dp))
            ScoreField(score)
            Spacer(Modifier.size(12.dp))
            ApplyButton(enabled) { addPoints(score.points) }
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
            NextPlayerButton(it)
        }
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
            pointsVariants = listOf(1, 2, 45)
        )
    }
}
