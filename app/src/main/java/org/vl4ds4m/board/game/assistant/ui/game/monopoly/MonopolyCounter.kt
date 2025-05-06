package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.annotation.StringRes
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.ui.detailedGameSessionPreview
import org.vl4ds4m.board.game.assistant.ui.game.component.NextPlayerButton
import org.vl4ds4m.board.game.assistant.ui.game.component.ResetButton
import org.vl4ds4m.board.game.assistant.ui.game.component.Score
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreField
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
    val panel = rememberSaveable { mutableStateOf(Panel.Positioning.name) }
    val navigatePanel: (Panel) -> Unit = { panel.value = it.name }
    val steps = rememberSaveable { mutableIntStateOf(2) }
    val money = rememberSaveable(saver = Score.Saver) { Score() }
    val sender = rememberSaveable(saver = TransferSide.Saver) { TransferSide() }
    val receiver = rememberSaveable(saver = TransferSide.Saver) { TransferSide() }
    when (panel.value) {
        Panel.Positioning.name -> {
            Positioning(
                navigate = navigatePanel,
                steps = steps,
                movePlayer = movePlayer,
                inPrison = inPrison,
                moveToPrison = moveToPrison,
                leavePrison = leavePrison,
                selectNextPlayer = selectNextPlayer,
                modifier = modifier
            )
        }
        Panel.Accounting.name -> {
            Accounting(
                navigate = navigatePanel,
                money = money,
                addMoney = addMoney,
                spendMoney = spendMoney,
                modifier = modifier
            )
        }
        Panel.Transferring.name -> {
            Transferring(
                navigate = navigatePanel,
                money = money,
                sender = sender,
                receiver = receiver,
                players = players,
                transferMoney = transferMoney,
                modifier = modifier
            )
        }
        else -> {}
    }
}

@Composable
private fun Positioning(
    navigate: (Panel) -> Unit,
    steps: MutableIntState,
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
            Slider(
                value = steps.intValue.toFloat(),
                onValueChange = { steps.intValue = it.roundToInt() },
                valueRange = 2f .. 12f,
                steps = 9,
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
        Navigation(
            destA = Panel.Accounting,
            destB = Panel.Transferring,
            navigate = navigate
        )
    }
}

@Composable
private fun Navigation(destA: Panel, destB: Panel, navigate: (Panel) -> Unit) {
    Row {
        OutlinedButton(
            onClick = { navigate(destA) },
            contentPadding = PaddingValues(6.dp),
            modifier = Modifier.width(120.dp)
        ) {
            Text(stringResource(destA.resId))
        }
        Spacer(Modifier.width(12.dp))
        OutlinedButton(
            onClick = { navigate(destB) },
            contentPadding = PaddingValues(6.dp),
            modifier = Modifier.width(120.dp)
        ) {
            Text(stringResource(destB.resId))
        }
    }
}

@Composable
private fun Accounting(
    navigate: (Panel) -> Unit,
    money: Score,
    addMoney: (Int) -> Unit,
    spendMoney: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ResetButton(money)
            ScoreField(
                score = money,
                label = "money"
            )
            val moneyFilled = remember {
                derivedStateOf { money.points > 0 }
            }
            CashButton(
                sign = Icons.Filled.KeyboardArrowUp,
                signDesc = "Spend",
                enabled = moneyFilled
            ) {
                spendMoney(money.points)
            }
            CashButton(
                sign = Icons.Default.Add,
                signDesc = "Add",
                enabled = moneyFilled
            ) {
                addMoney(money.points)
            }
        }
        Navigation(
            destA = Panel.Positioning,
            destB = Panel.Transferring,
            navigate = navigate
        )
    }
}

@Composable
private fun CashButton(
    sign: ImageVector,
    signDesc: String,
    enabled: State<Boolean>,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enabled.value,
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
                imageVector = sign,
                contentDescription = signDesc,
                modifier = Modifier
                    .size(20.dp)
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

@Composable
private fun Transferring(
    navigate: (Panel) -> Unit,
    money: Score,
    sender: TransferSide,
    receiver: TransferSide,
    players: State<Players>,
    transferMoney: (PID, PID, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ResetButton(money)
            ScoreField(
                score = money,
                label = "money"
            )
            val enabled = remember {
                derivedStateOf {
                    money.points > 0 && sender.ready && receiver.ready
                }
            }
            IconButton(
                onClick = { transferMoney(sender.id, receiver.id, money.points) },
                enabled = enabled.value,
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TransferParticipant(
                side = sender,
                label = stringResource(R.string.game_monopoly_from_player),
                players = players
            )
            TransferParticipant(
                side = receiver,
                label = stringResource(R.string.game_monopoly_to_player),
                players = players
            )

        }
        Navigation(
            destA = Panel.Positioning,
            destB = Panel.Accounting,
            navigate = navigate
        )
    }
}

@Composable
private fun TransferParticipant(
    side: TransferSide,
    label: String,
    players: State<Players>
) {
    val expanded = remember { mutableStateOf(false) }
    Box {
        FilledTonalButton(
            onClick = { expanded.value = true },
            contentPadding = PaddingValues(6.dp),
            modifier = Modifier.width(120.dp)
        ) {
            Text(
                text = if (side.ready) side.name else label,
                maxLines = 1
            )
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                side.reset()
                expanded.value = false
            }
        ) {
            for ((id, player) in players.value) {
                DropdownMenuItem(
                    text = { Text(player.name) },
                    onClick = {
                        side.id = id
                        side.name = player.name
                        expanded.value = false
                    }
                )
            }
        }
    }
}

private enum class Panel(
    @StringRes val resId: Int
) {
    Positioning(R.string.game_monopoly_positioning),
    Accounting(R.string.game_monopoly_accounting),
    Transferring(R.string.game_monopoly_transferring)
}

@Stable
private class TransferSide {
    private val mId = mutableIntStateOf(EMPTY_PID)
    var id: PID
        get() = mId.intValue
        set(value) { mId.intValue = value }

    private val mName: MutableState<String> = mutableStateOf(EMPTY_NAME)
    var name: String
        get() = mName.value
        set(value) { mName.value = value }

    val ready: Boolean get() = id != EMPTY_PID

    fun reset() {
        id = EMPTY_PID
        name = EMPTY_NAME
    }

    companion object {
        private const val EMPTY_PID: PID = -1
        private const val EMPTY_NAME: String = ""

        val Saver = mapSaver(
            save = {
                mapOf("id" to it.id, "name" to it.name)
            },
            restore = { map ->
                TransferSide().apply {
                    id = (map["id"] as? PID) ?: EMPTY_PID
                    name = (map["name"] as? String) ?: EMPTY_NAME
                }
            }
        )
    }
}

@Preview
@Composable
private fun MonopolyPositioningPreview() {
    BoardGameAssistantTheme {
        Positioning(
            navigate = {},
            steps = remember { mutableIntStateOf(6) },
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
            navigate = {},
            money = Score().apply { text = "450" },
            addMoney = {},
            spendMoney = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun MonopolyTransferringPreview() {
    BoardGameAssistantTheme {
        Transferring(
            navigate = {},
            money = Score(),
            sender = TransferSide().apply {
                id = 1
                name = "Player A"
            },
            receiver = TransferSide().apply {
                id = 2
                name = "Player B"
            },
            players = rememberUpdatedState(
                detailedGameSessionPreview.players.toMap()
            ),
            transferMoney = { _, _, _ -> },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
