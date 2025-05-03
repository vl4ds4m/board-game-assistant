package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.monopoly.MonopolyGame
import org.vl4ds4m.board.game.assistant.ui.game.GameUI
import org.vl4ds4m.board.game.assistant.ui.game.PlayerStats

class MonopolyGameUI private constructor(game: MonopolyGame) :
    GameUI by GameUI.create(game)
{
    override val playerStats: PlayerStats = MonopolyGameUI.playerStats

    override val masterActions: @Composable () -> Unit = {
        MonopolyCounter(
            players = game.players.collectAsState(),
            movePlayer = game::movePlayer,
            inPrison = game.inPrison.collectAsState(),
            moveToPrison = game::moveToPrison,
            leavePrison = { game.leavePrison(true) },
            selectNextPlayer = game::changeCurrentPlayerId,
            addMoney = game::addMoney,
            spendMoney = game::spendMoney,
            transferMoney = game::transferMoney
        )
    }

    override val actionPresenter = MonopolyGameUI.actionPresenter

    companion object : GameUI.Factory by GameUI {
        override fun create(game: Game) = MonopolyGameUI(game as MonopolyGame)

        override val playerStats: PlayerStats = {
            MonopolyPlayerStats(it)
        }

        override val actionPresenter = MonopolyGameActionPresenter
    }
}
