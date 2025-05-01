package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel

class MonopolyGameUI private constructor(vm: MonopolyGameViewModel) :
    GameUI by GameUI.create(vm, false)
{
    override val masterActions: @Composable () -> Unit = {
        MonopolyCounter(
            players = vm.players.collectAsState(),
            movePlayer = vm::movePlayer,
            inPrison = vm.inPrison.collectAsState(),
            moveToPrison = vm::moveToPrison,
            leavePrison = { vm.leavePrison(true) },
            selectNextPlayer = vm::changeCurrentPlayerId,
            addMoney = vm::addMoney,
            spendMoney = vm::spendMoney,
            transferMoney = vm::transferMoney
        )
    }

    override val actionPresenter = MonopolyGameActionPresenter

    companion object : GameUI.Factory {
        override fun create(viewModel: GameViewModel): GameUI =
            MonopolyGameUI(viewModel as MonopolyGameViewModel)
    }
}
