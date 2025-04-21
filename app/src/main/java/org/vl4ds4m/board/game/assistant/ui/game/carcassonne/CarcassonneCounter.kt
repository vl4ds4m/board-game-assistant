package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneProperty
import org.vl4ds4m.board.game.assistant.ui.game.component.ApplyButton
import org.vl4ds4m.board.game.assistant.ui.game.component.NextPlayerButton
import org.vl4ds4m.board.game.assistant.ui.game.component.PointsAppender
import org.vl4ds4m.board.game.assistant.ui.game.component.ResetButton
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreField
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme

@Composable
fun CarcassonneCounter(
    addPoints: (CarcassonneProperty, Int, Boolean) -> Unit,
    selectNextPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val score = rememberSaveable { mutableIntStateOf(0) }
        val property = rememberSaveable {
            mutableStateOf<CarcassonneProperty?>(null)
        }
        val finalStage = rememberSaveable { mutableStateOf(false) }
        val applyEnable = remember {
            derivedStateOf { score.intValue != 0 && property.value != null }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ResetButton { score.intValue = 0 }
            Spacer(Modifier.size(16.dp))
            ScoreField(score)
            Spacer(Modifier.size(24.dp))
            ApplyButton(applyEnable) apl@{
                val prop = property.value ?: return@apl
                val sc = score.intValue.takeIf { it != 0 }
                    ?: return@apl
                addPoints(prop, sc, finalStage.value)
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PointsAppender(
                pointsVariants = listOf(1, 5),
                score = score
            )
            PropertyButton(property)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NextPlayerButton(selectNextPlayer)
            FinalStageButton(finalStage)
        }
    }
}

@Composable
private fun PropertyButton(property: MutableState<CarcassonneProperty?>) {
    var expanded by remember { mutableStateOf(false) }
    OutlinedButton(
        onClick = { expanded = true }
    ) {
        Text(
            text =  stringResource(
                property.value?.localizedStringRes ?: R.string.game_master_carcassonne_property
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (entry in CarcassonneProperty.entries) {
                DropdownMenuItem(
                    text = { Text(stringResource(entry.localizedStringRes)) },
                    onClick = {
                        property.value = entry
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FinalStageButton(finalStage: MutableState<Boolean>) {
    OutlinedButton(
        onClick = { finalStage.value = !finalStage.value }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.game_master_carcassonne_final)
            )
            Spacer(Modifier.size(8.dp))
            Checkbox(
                checked = finalStage.value,
                onCheckedChange = null
            )
        }
    }
}

@Preview
@Composable
private fun CarcassonneCounterPreview() {
    BoardGameAssistantTheme {
        CarcassonneCounter(
            addPoints = { _, _, _ -> },
            selectNextPlayer = {}
        )
    }
}
