package org.vl4ds4m.board.game.assistant.game.log

import org.vl4ds4m.board.game.assistant.game.Game

interface GameAction {
    fun revert(game: Game)

    fun repeat(game: Game)
}
