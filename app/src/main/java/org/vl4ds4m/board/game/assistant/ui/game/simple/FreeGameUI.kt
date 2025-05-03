package org.vl4ds4m.board.game.assistant.ui.game.simple

import androidx.compose.runtime.Composable
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.simple.FreeGame
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.component.StandardCounter

class FreeGameUI private constructor(game: FreeGame) :
    GameUI by GameUI.createBaseUi(game, true)
{
    override val masterActions: @Composable () -> Unit = {
        StandardCounter(
            addPoints = game::addPoints,
            applyEnabled = null,
            selectNextPlayer = null
        )
    }

    companion object : GameUI.Factory by GameUI {
        override fun create(game: Game) = FreeGameUI(game as FreeGame)
    }
}
