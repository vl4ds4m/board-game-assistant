package org.vl4ds4m.board.game.assistant.ui.game.simple

import androidx.compose.runtime.Composable
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGame
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.component.StandardCounter

class SimpleOrderedGameUI private constructor(game: SimpleOrderedGame) :
    GameUI by GameUI.create(game)
{
    override val masterActions: @Composable () -> Unit = {
        StandardCounter(
            addPoints = game::addPoints,
            applyEnabled = null,
            selectNextPlayer = game::changeCurrentPid
        )
    }

    companion object : GameUI.Factory by GameUI {
        override fun create(game: Game) = SimpleOrderedGameUI(game as SimpleOrderedGame)
    }
}
