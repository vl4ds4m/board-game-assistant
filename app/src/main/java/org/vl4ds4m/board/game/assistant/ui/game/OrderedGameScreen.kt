package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
