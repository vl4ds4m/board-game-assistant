package org.vl4ds4m.board.game.assistant.ui.game.ordered

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.domain.game.Carcassonne
import org.vl4ds4m.board.game.assistant.domain.game.Dice
import org.vl4ds4m.board.game.assistant.domain.game.Monopoly
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGameType
import org.vl4ds4m.board.game.assistant.domain.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.GameMenuActions
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.dice.DiceGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

@Composable
fun OrderedGameScreen(
    type: OrderedGameType,
    viewModel: GameViewModel,
    menuActions: GameMenuActions,
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
                menuActions = menuActions,
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
                menuActions = menuActions,
                modifier = modifier
            )
        }
        is Carcassonne -> {
            CarcassonneGameScreen(
                viewModel = viewModel as CarcassonneGameViewModel,
                menuActions = menuActions,
                modifier = modifier
            )
        }
        is Monopoly -> {
            MonopolyGameScreen(
                viewModel = viewModel as MonopolyGameViewModel,
                menuActions = menuActions,
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
    menuActions: GameMenuActions,
    modifier: Modifier = Modifier
) {
    GameScreen(
        viewModel = viewModel,
        onNameFormat = onNameFormat,
        currentPlayerId = viewModel.currentPlayerId.collectAsState(),
        onSelectPlayer = null,
        masterActions = masterActions,
        menuActions = menuActions,
        modifier = modifier
    )
}
