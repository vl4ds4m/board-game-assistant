package org.vl4ds4m.board.game.assistant.game.log

import org.vl4ds4m.board.game.assistant.game.Game

abstract class BaseGameAction(
    private val revertAction: Game.() -> Unit,
    private val repeatAction: Game.() -> Unit
) : GameAction {
    override fun revert(game: Game) {
        game.revertAction()
    }

    override fun repeat(game: Game) {
        game.repeatAction()
    }
}
