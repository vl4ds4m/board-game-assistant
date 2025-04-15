package org.vl4ds4m.board.game.assistant.ui.game.ordered

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen

@Composable
fun OrderedGameScreen(
    modifier: Modifier = Modifier,
    masterActions: @Composable () -> Unit
) {
    GameScreen(
        selectPlayer = null,
        modifier = modifier,
        masterActions = masterActions
    )
}
