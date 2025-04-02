package org.vl4ds4m.board.game.assistant.ui.game.free

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.vl4ds4m.board.game.assistant.ui.game.GameScreen
import org.vl4ds4m.board.game.assistant.ui.game.component.GameNavActions
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter
import org.vl4ds4m.board.game.assistant.ui.game.vm.GameViewModel

@Composable
fun FreeGameScreen(
    sessionId: String?,
    navActions: GameNavActions,
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel<GameViewModel>(
        factory = FreeGameViewModel.createFactory(sessionId)
    ) as FreeGameViewModel
    GameScreen(
        viewModel = viewModel,
        onNameFormat = { "$it (free)" },
        currentPlayerId = viewModel.currentPlayerId,
        onSelectPlayer = viewModel::changeCurrentPlayerId,
        masterActions = {
            ScoreCounter(
                onPointsAdd = viewModel::addPoints
            )
        },
        navActions = navActions,
        modifier = modifier
    )
}
