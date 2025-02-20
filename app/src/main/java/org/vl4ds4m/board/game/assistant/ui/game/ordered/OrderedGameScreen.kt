package org.vl4ds4m.board.game.assistant.ui.game.ordered

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    viewModel: GameViewModel,
    navActions: GameNavActions,
    modifier: Modifier = Modifier
) {
    when (type) {
        is SimpleOrdered -> {
            OrderedGameScreen(
                viewModel = viewModel as SimpleOrderedGameViewModel,
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
            OrderedGameScreen(
                viewModel = viewModel as DiceGameViewModel,
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
            CarcassonneGameScreen(
                viewModel = viewModel as CarcassonneGameViewModel,
                navActions = navActions,
                modifier = modifier
            )
        }
        is Monopoly -> {
            MonopolyGameScreen(
                viewModel = viewModel as MonopolyGameViewModel,
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
