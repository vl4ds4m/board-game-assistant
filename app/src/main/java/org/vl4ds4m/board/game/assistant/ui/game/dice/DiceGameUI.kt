package org.vl4ds4m.board.game.assistant.ui.game.dice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.vl4ds4m.board.game.assistant.game.DiceGame
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.component.ScoreCounter

class DiceGameUI private constructor(game: DiceGame) :
    GameUI by GameUI.create(game)
{
    override val masterActions: @Composable () -> Unit = {
        ScoreCounter(
            addPoints = game::addPoints,
            applyEnabled = game.addEnabled.collectAsState(),
            selectNextPlayer = game::changeCurrentPid,
            pointsVariants = listOf(5, 10, 50)
        )
    }

    companion object : GameUI.Factory by GameUI {
        override fun create(game: Game) = DiceGameUI(game as DiceGame)
    }
}
