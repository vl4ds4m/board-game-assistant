package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.ui.game.component.NextPlayerButton
import org.vl4ds4m.board.game.assistant.ui.game.component.ResetButton
import org.vl4ds4m.board.game.assistant.ui.game.component.Score
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreField
import org.vl4ds4m.board.game.assistant.ui.game.previewPlayers
import org.vl4ds4m.board.game.assistant.ui.theme.BoardGameAssistantTheme
import kotlin.math.roundToInt

@Composable
fun MonopolyCounter(
    players: State<Players>,
    movePlayer: (Int) -> Unit,
    inPrison: State<Boolean>,
    moveToPrison: () -> Unit,
    leavePrison: () -> Unit,
    selectNextPlayer: () -> Unit,
    addMoney: (Int) -> Unit,
    spendMoney: (Int) -> Unit,
    transferMoney: (PID, PID, Int) -> Unit,
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
            players = players,
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
    players: State<Players>,
    addMoney: (Int) -> Unit,
    spendMoney: (Int) -> Unit,
    transferMoney: (PID, PID, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val money = rememberSaveable(saver = Score.Saver) { Score() }
        val moneyFilled = remember {
            derivedStateOf { money.points > 0 }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ResetButton(money)
            ScoreField(
                score = money,
                label = "money"
            )
            IconButton(
                onClick = { spendMoney(money.points) },
                enabled = moneyFilled.value,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = MaterialTheme.shapes.large
                    )
                    .height(44.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.keyboard_arrow_down_24px),
                        contentDescription = "Arrow up",
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(180f)
                            .align(Alignment.TopCenter)
                    )
                    Icon(
                        painter = painterResource(R.drawable.credit_card_24px),
                        contentDescription = "Credit card",
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
            IconButton(
                onClick = { addMoney(money.points) },
                enabled = moneyFilled.value,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = MaterialTheme.shapes.large
                    )
                    .height(44.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.keyboard_arrow_down_24px),
                        contentDescription = "Arrow down",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopCenter)
                    )
                    Icon(
                        painter = painterResource(R.drawable.credit_card_24px),
                        contentDescription = "Credit card",
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val fromId = rememberSaveable { mutableIntStateOf(NIL_PID) }
            val fromName = rememberSaveable { mutableStateOf("") }
            TransferParticipant(
                player = fromName,
                label = stringResource(R.string.game_monopoly_from_player),
                players = players,
                takePlayer = { id, name ->
                    fromId.intValue = id
                    fromName.value = name
                }
            )
            val toId = rememberSaveable { mutableIntStateOf(NIL_PID) }
            val toName = rememberSaveable { mutableStateOf("") }
            TransferParticipant(
                player = toName,
                label = stringResource(R.string.game_monopoly_to_player),
                players = players,
                takePlayer = { id, name ->
                    toId.intValue = id
                    toName.value = name
                }
            )
            IconButton(
                onClick = {
                    val sender = fromId.intValue
                    val receiver = toId.intValue
                    val amount = money.points
                    if (sender != NIL_PID && receiver != NIL_PID && amount > 0) {
                        transferMoney(sender, receiver, amount)
                    }
                },
                enabled = moneyFilled.value,
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.large
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.double_arrow_24px),
                    contentDescription = "Transfer",
                    modifier = Modifier.size(32.dp)
                )
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
    player: State<String>,
    label: String,
    players: State<Players>,
    takePlayer: (PID, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    Box(modifier) {
        FilledTonalButton(
            onClick = { expanded.value = true },
            contentPadding = PaddingValues(6.dp),
            modifier = Modifier.width(100.dp)
        ) {
            Text(
                text = player.value.ifBlank { label },
                maxLines = 1
            )
        }
        PlayerSelector(
            expanded = expanded,
            players = players,
            takePlayer = takePlayer
        )
    }
}

@Composable
private fun PlayerSelector(
    expanded: MutableState<Boolean>,
    players: State<Players>,
    takePlayer: (PID, String) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = {
            takePlayer(NIL_PID, "")
            expanded.value = false
        },
        modifier = modifier
    ) {
        for ((id, player) in players.value) {
            DropdownMenuItem(
                text = { Text(player.name) },
                onClick = {
                    takePlayer(id, player.name)
                    expanded.value = false
                }
            )
        }
    }
}

private const val NIL_PID: PID = -1

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
            selectNextPlayer = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun MonopolyAccountingPreview() {
    BoardGameAssistantTheme {
        Accounting(
            toPositioning = {},
            players = rememberUpdatedState(previewPlayers),
            addMoney = {},
            spendMoney = {},
            transferMoney = { _, _, _ -> },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
