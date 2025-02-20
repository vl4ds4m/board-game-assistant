package org.vl4ds4m.board.game.assistant.game.log

import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.state.PlayerState

class PlayerStateChangeAction(
    val playerId: Long,
    val oldState: PlayerState,
    val newState: PlayerState,
    revert: Game.() -> Unit,
    repeat: Game.() -> Unit
) : BaseGameAction(revertAction = revert, repeatAction = repeat)
