package org.vl4ds4m.board.game.assistant.game.log

import org.vl4ds4m.board.game.assistant.game.Game

class CurrentPlayerChangeAction(
    val oldPlayerId: Long?,
    val newPlayerId: Long?,
    revert: Game.() -> Unit,
    repeat: Game.() -> Unit
) : BaseGameAction(revertAction = revert, repeatAction = repeat)
