package org.vl4ds4m.board.game.assistant.ui.game.ordered

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.ui.game.component.StandardCounter
import org.vl4ds4m.board.game.assistant.ui.game.GameViewModel

@Composable
fun SimpleOrderedGameScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<GameViewModel>() as SimpleOrderedGameViewModel
    OrderedGameScreen(modifier) {
        StandardCounter(
            addPoints = viewModel::addPoints,
            applyEnabled = null,
            selectNextPlayer = viewModel::changeCurrentPlayerId
        )
    }
}
