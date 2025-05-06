package org.vl4ds4m.board.game.assistant.ui.game.carcassonne

import androidx.compose.runtime.Composable
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.carcassonne.CarcassonneGame
import org.vl4ds4m.board.game.assistant.ui.game.GameUI

class CarcassonneGameUI private constructor(game: CarcassonneGame) :
    GameUI by GameUI.create(game)
{
    override val masterActions: @Composable () -> Unit = {
        CarcassonneCounter(
            addPoints = game::addPoints,
            selectNextPlayer = game::changeCurrentPid
        )
    }

    companion object : GameUI.Factory by GameUI {
        override fun create(game: Game) = CarcassonneGameUI(game as CarcassonneGame)
    }
}
