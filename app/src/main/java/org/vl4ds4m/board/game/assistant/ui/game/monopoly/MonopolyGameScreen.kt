package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.GameModifier
import org.vl4ds4m.board.game.assistant.ui.game.ordered.OrderedGameScreen

@Composable
fun MonopolyGameScreen(
    viewModel: MonopolyGameViewModel,
    gameModifier: GameModifier,
    modifier: Modifier = Modifier,
) {
    OrderedGameScreen(
        viewModel = viewModel,
        onNameFormat = { "Monopoly '$it'" },
        masterActions = {},
        gameModifier = gameModifier,
        modifier = modifier
    )
}
