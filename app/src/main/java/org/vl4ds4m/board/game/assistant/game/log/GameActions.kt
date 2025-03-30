package org.vl4ds4m.board.game.assistant.game.log

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

sealed interface GameAction {
    companion object {
        const val CHANGE_CURRENT_PLAYER  = "change_current_player"
        const val CHANGE_PLAYER_STATE  = "change_player_state"
    }
}

@Serializable
data class CurrentPlayerChangeAction(
    val oldPlayerId: Long?,
    val newPlayerId: Long?
) : GameAction

@Serializable
data class PlayerStateChangeAction(
    val playerId: Long,
    val oldState: PlayerState,
    val newState: PlayerState
) : GameAction
