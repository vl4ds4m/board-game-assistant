package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.ui.game.component.StandardCounter

typealias PlayerStats = @Composable RowScope.(State<PlayerState>) -> Unit

interface GameUI {
    val playerStats: PlayerStats

    val onPlayerSelected: ((PID) -> Unit)?

    val masterActions: @Composable () -> Unit

    val actionLog: ActionLog

    interface Factory {
        fun create(game: Game): GameUI

        val playerStats: PlayerStats

        val actionLog: ActionLog
    }

    companion object : Factory {
        override fun create(game: Game): GameUI = createBaseUi(game, false)

        override val playerStats: PlayerStats = { state ->
            Spacer(Modifier.weight(1f))
            Text("${state.value.score} point(s)")
        }
        
        override val actionLog: ActionLog =
            GameActionPresenter.actionLog
        
        fun createBaseUi(game: Game, selectAllowed: Boolean): GameUI =
            BaseGameUI(game, selectAllowed)

        val masterActionsPreview: @Composable () -> Unit = {
            StandardCounter(
                addPoints = {},
                applyEnabled = null,
                selectNextPlayer = {}
            )
        }
    }
}

private class BaseGameUI(game: Game, selectAllowed: Boolean) : GameUI {
    override val playerStats: PlayerStats = GameUI.playerStats

    override val onPlayerSelected = if (selectAllowed) {
        game::changeCurrentPid
    } else {
        null
    }

    override val masterActions: @Composable () -> Unit = {}

    override val actionLog = GameUI.actionLog
}
