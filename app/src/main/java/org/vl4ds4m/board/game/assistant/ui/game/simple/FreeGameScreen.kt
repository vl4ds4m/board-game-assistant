package org.vl4ds4m.board.game.assistant.ui.game.simple

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.StandardCounter
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel

@Composable
fun FreeGameScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>() as FreeGameViewModel
    GameScreen(
        selectPlayer = viewModel::changeCurrentPlayerId,
        modifier = modifier
    ) {
        StandardCounter(
            addPoints = viewModel::addPoints,
            applyEnabled = null,
            selectNextPlayer = null
        )
    }
}
