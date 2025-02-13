package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.OrderedGameScreen

@Composable
fun MonopolyGameScreen(
    viewModel: MonopolyGameViewModel,
    onGameComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OrderedGameScreen(
        viewModel = viewModel,
        onGameComplete = onGameComplete,
        masterActions = {},
        modifier = modifier
    )
}
