package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen

@Composable
fun MonopolyGameScreen(
    viewModel: MonopolyGameViewModel,
    navActions: GameNavActions,
    modifier: Modifier = Modifier,
) {
    OrderedGameScreen(
        viewModel = viewModel,
        onNameFormat = { "Monopoly '$it'" },
        masterActions = {},
        navActions = navActions,
        modifier = modifier
    )
}
