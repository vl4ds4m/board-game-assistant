package org.vl4ds4m.board.game.assistant.ui.game.ordered

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.game.Carcassonne
import org.vl4ds4m.board.game.assistant.game.Dice
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.OrderedGameType
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.dice.DiceGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

@Composable
fun OrderedGameScreen(
    type: OrderedGameType,
    sessionId: String?,
    navActions: GameNavActions,
    modifier: Modifier = Modifier
) {
    when (type) {
        is SimpleOrdered -> {
            val viewModel = viewModel<GameViewModel>(
                factory = SimpleOrderedGameViewModel.createFactory(sessionId)
            ) as SimpleOrderedGameViewModel
            OrderedGameScreen(
                viewModel = viewModel,
                onNameFormat = { "$it (ordered)" },
                masterActions = {
                    ScoreCounter(
                        onPointsAdd = viewModel::addPoints
                    )
                },
                navActions = navActions,
                modifier = modifier
            )
        }
        is Dice -> {
            val viewModel = viewModel<GameViewModel>(
                factory = DiceGameViewModel.createFactory(sessionId)
            ) as DiceGameViewModel
            OrderedGameScreen(
                viewModel = viewModel,
                onNameFormat = { "Dice '$it'" },
                masterActions = {
                    ScoreCounter(
                        onPointsAdd = viewModel::addPoints
                    )
                },
                navActions = navActions,
                modifier = modifier
            )
        }
        is Carcassonne -> {
            val viewModel = viewModel<GameViewModel>(
                factory = CarcassonneGameViewModel.createFactory(sessionId)
            ) as CarcassonneGameViewModel
            CarcassonneGameScreen(
                viewModel = viewModel,
                navActions = navActions,
                modifier = modifier
            )
        }
        is Monopoly -> {
            val viewModel = viewModel<GameViewModel>(
                factory = MonopolyGameViewModel.createFactory(sessionId)
            ) as MonopolyGameViewModel
            MonopolyGameScreen(
                viewModel = viewModel,
                navActions = navActions,
                modifier = modifier
            )
        }
    }
}

@Composable
fun OrderedGameScreen(
    viewModel: OrderedGameViewModel,
    onNameFormat: (String) -> String,
    masterActions: @Composable () -> Unit,
    navActions: GameNavActions,
    modifier: Modifier = Modifier
) {
    GameScreen(
        viewModel = viewModel,
        onNameFormat = onNameFormat,
        currentPlayerId = viewModel.currentPlayerId,
        onSelectPlayer = null,
        masterActions = masterActions,
        navActions = navActions,
        modifier = modifier
    )
}
