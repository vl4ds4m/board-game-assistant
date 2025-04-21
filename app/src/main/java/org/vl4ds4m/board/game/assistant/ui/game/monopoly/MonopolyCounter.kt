package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.ui.game.component.NextPlayerButton
import org.vl4ds4m.board.game.assistant.ui.game.component.ResetButton
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreField
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme
import kotlin.math.roundToInt

@Composable
fun MonopolyCounter(
    movePlayer: (Int) -> Unit,
    inPrison: State<Boolean>,
    moveToPrison: () -> Unit,
    leavePrison: () -> Unit,
    selectNextPlayer: () -> Unit,
    addMoney: (Int) -> Unit,
    spendMoney: (Int) -> Unit,
    transferMoney: (Long, Long, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstPanel = rememberSaveable { mutableStateOf(true) }
    if (firstPanel.value) {
        Positioning(
            toAccounting = { firstPanel.value = false },
            movePlayer = movePlayer,
            inPrison = inPrison,
            moveToPrison = moveToPrison,
            leavePrison = leavePrison,
            selectNextPlayer = selectNextPlayer,
            modifier = modifier
        )
    } else {
        Accounting(
            toPositioning = { firstPanel.value = true },
            addMoney = addMoney,
            spendMoney = spendMoney,
            transferMoney = transferMoney,
            modifier = modifier
        )
    }
}

@Composable
private fun Positioning(
    toAccounting: () -> Unit,
    movePlayer: (Int) -> Unit,
    inPrison: State<Boolean>,
    moveToPrison: () -> Unit,
    leavePrison: () -> Unit,
    selectNextPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val steps = rememberSaveable { mutableIntStateOf(1) }
            Slider(
                value = steps.intValue.toFloat(),
                onValueChange = { steps.intValue = it.roundToInt() },
                valueRange = 1f .. 12f,
                steps = 10,
                modifier = Modifier.width(200.dp)
            )
            Spacer(Modifier.size(24.dp))
            Button(
                onClick = { movePlayer(steps.intValue) },
                modifier = Modifier.width(120.dp)
            ) {
                Text(
                    text = stringResource(R.string.game_monopoly_move_prefix)
                        + " ${steps.intValue}"
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (inPrison.value) leavePrison()
                    else moveToPrison()
                }
            ) {
                Text(
                    text = if (inPrison.value) {
                        stringResource(R.string.game_monopoly_from_prison)
                    } else {
                        stringResource(R.string.game_monopoly_to_prison)
                    }
                )
            }
            NextPlayerButton(
                onClick = selectNextPlayer,
                modifier = Modifier.width(130.dp)
            )
        }
        OutlinedButton(
            onClick = toAccounting,
            modifier = Modifier.width(180.dp)
        ) {
            Text(stringResource(R.string.game_monopoly_accounting))
        }
    }
}

@Composable
private fun Accounting(
    toPositioning: () -> Unit,
    addMoney: (Int) -> Unit,
    spendMoney: (Int) -> Unit,
    transferMoney: (Long, Long, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val money = rememberSaveable { mutableIntStateOf(0) }
        val moneyFilled = remember {
            derivedStateOf { money.intValue > 0 }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ResetButton { money.intValue = 0 }
            ScoreField(
                score = money,
                label = "money",
                modifier = Modifier
                    .width(110.dp)
            )
            Button(
                onClick = { spendMoney(money.intValue) },
                enabled = moneyFilled.value
            ) {
                Text(stringResource(R.string.game_monopoly_pay))
            }
            Button(
                onClick = { addMoney(money.intValue) },
                enabled = moneyFilled.value
            ) {
                Text(stringResource(R.string.game_monopoly_gain))
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TransferParticipant(stringResource(R.string.game_monopoly_from_player))
            TransferParticipant(stringResource(R.string.game_monopoly_to_player))
            Button(
                onClick = { transferMoney(-1L, -1L, -1) }, // TODO Implement transferring
                enabled = moneyFilled.value
            ) {
                Text(stringResource(R.string.game_monopoly_transfer))
            }
        }
        OutlinedButton(
            onClick = toPositioning,
            modifier = Modifier.width(180.dp)
        ) {
            Text(stringResource(R.string.game_monopoly_positioning))
        }
    }
}

@Composable
private fun TransferParticipant(
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(label) },
        singleLine = true,
        modifier = modifier.width(100.dp)
    )
}

@Preview
@Composable
private fun MonopolyPositioningPreview() {
    BoardGameAssistantTheme {
        Positioning(
            toAccounting = {},
            movePlayer = {},
            inPrison = rememberUpdatedState(false),
            moveToPrison = {},
            leavePrison = {},
            selectNextPlayer = {}
        )
    }
}

@Preview
@Composable
private fun MonopolyAccountingPreview() {
    BoardGameAssistantTheme {
        Accounting(
            toPositioning = {},
            addMoney = {},
            spendMoney = {},
            transferMoney = { _, _, _ -> },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
