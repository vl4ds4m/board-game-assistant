package org.vl4ds4m.board.game.assistant.game.log

import org.vl4ds4m.board.game.assistant.game.state.PlayerState

sealed interface GameAction

data class CurrentPlayerChangeAction(
    val oldPlayerId: Long?,
    val newPlayerId: Long?
) : GameAction

data class PlayerStateChangeAction(
    val playerId: Long,
    val oldState: PlayerState,
    val newState: PlayerState
) : GameAction
