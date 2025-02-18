package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.component.GameMenuActions
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen

@Composable
fun MonopolyGameScreen(
    viewModel: MonopolyGameViewModel,
    menuActions: GameMenuActions,
    modifier: Modifier = Modifier,
) {
    OrderedGameScreen(
        viewModel = viewModel,
        onNameFormat = { "Monopoly '$it'" },
        masterActions = {},
        menuActions = menuActions,
        modifier = modifier
    )
}
