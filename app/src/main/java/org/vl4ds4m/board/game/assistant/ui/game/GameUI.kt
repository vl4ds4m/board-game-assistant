package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

typealias PlayerStats = @Composable RowScope.(State<PlayerState>) -> Unit

interface GameUI {
    val playerStats: PlayerStats

    val onPlayerSelected: ((Long) -> Unit)?

    val masterActions: @Composable () -> Unit

    val actionPresenter: GameActionPresenter

    interface Factory {
        fun create(viewModel: GameViewModel): GameUI
    }

    companion object {
        fun create(
            viewModel: GameViewModel,
            selectAllowed: Boolean
        ): GameUI = BaseGameUI(viewModel, selectAllowed)
    }
}

private class BaseGameUI(
    viewModel: GameViewModel,
    selectAllowed: Boolean
) : GameUI {
    override val playerStats: PlayerStats = { state: State<PlayerState> ->
        Spacer(Modifier.weight(1f))
        Text("${state.value.score} point(s)")
    }

    override val onPlayerSelected = if (selectAllowed) {
        viewModel::changeCurrentPlayerId
    } else {
        null
    }

    override val masterActions: @Composable () -> Unit = {}

    override val actionPresenter = GameActionPresenter.Default
}
