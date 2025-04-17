package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel

@Composable
fun MonopolyGameScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>() as MonopolyGameViewModel
    OrderedGameScreen(modifier) {
        MonopolyCounter(
            movePlayer = viewModel::movePlayer,
            inPrison = viewModel.inPrison.collectAsState(),
            moveToPrison = viewModel::moveToPrison,
            leavePrison = { viewModel.leavePrison(true) },
            selectNextPlayer = viewModel::changeCurrentPlayerId,
            addMoney = viewModel::addMoney,
            spendMoney = viewModel::spendMoney,
            transferMoney = viewModel::transferMoney
        )
    }
}
