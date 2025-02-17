package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.domain.game.Carcassonne
import org.vl4ds4m.board.game.assistant.domain.game.Dice
import org.vl4ds4m.board.game.assistant.domain.game.Monopoly
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGameType
import org.vl4ds4m.board.game.assistant.domain.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.ui.game.carcassonne.CarcassonneGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.dice.DiceGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.monopoly.MonopolyGameScreen
import org.vl4ds4m.board.game.assistant.ui.game.ordered.SimpleOrderedGameViewModel
import org.vl4ds4m.board.game.assistant.ui.game.vm.OrderedGameViewModel

@Composable
fun OrderedGameScreen(
    type: OrderedGameType,
    viewModelFactory: ViewModelProvider.Factory,
    onGameComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (type) {
        is SimpleOrdered -> {
            val viewModel = viewModel<SimpleOrderedGameViewModel>(
                factory = viewModelFactory
            )
            OrderedGameScreen(
                viewModel = viewModel,
                onGameComplete = onGameComplete,
                masterActions = {
                    ScoreCounter(
                        onPointsAdd = viewModel::addPoints
                    )
                },
                modifier = modifier
            )
        }
        is Dice -> {
            val viewModel = viewModel<DiceGameViewModel>(
                factory = viewModelFactory
            )
            OrderedGameScreen(
                viewModel = viewModel,
                onGameComplete = onGameComplete,
                masterActions = {
                    ScoreCounter(
                        onPointsAdd = viewModel::addPoints
                    )
                },
                modifier = modifier
            )
        }
        is Carcassonne -> {
            CarcassonneGameScreen(
                viewModel = viewModel(
                    factory = viewModelFactory
                ),
                onGameComplete = onGameComplete,
                modifier = modifier
            )
        }
        is Monopoly -> {
            MonopolyGameScreen(
                viewModel = viewModel(
                    factory = viewModelFactory
                ),
                onGameComplete = onGameComplete,
                modifier = modifier
            )
        }
    }
}

@Composable
fun OrderedGameScreen(
    viewModel: OrderedGameViewModel,
    onGameComplete: () -> Unit,
    masterActions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    GameScreen(
        viewModel = viewModel,
        onGameComplete = onGameComplete,
        currentPlayerId = viewModel.currentPlayerId.collectAsState(),
        onSelectPlayer = null,
        masterActions = masterActions,
        modifier = modifier
    )
}
