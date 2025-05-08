package org.vl4ds4m.board.game.assistant.game

import org.vl4ds4m.board.game.assistant.States
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.log.GameAction

fun currentPlayerChangedAction(ids: States<PID?>) = GameAction(
    type = CHANGE_CURRENT_PLAYER,
    data = mapOf(
        OLD_PLAYER_ID_KEY to (ids.prev ?: NIL_ID).toString(),
        NEW_PLAYER_ID_KEY to (ids.next ?: NIL_ID).toString()
    )
)

val GameAction.changesCurrentPlayer: Boolean
    get() = type == CHANGE_CURRENT_PLAYER

val GameAction.currentPIDs: States<PID?>?
    get() {
        val prevId = data[OLD_PLAYER_ID_KEY]
            ?.toIntOrNull()
            ?: return null
        val nextId = data[NEW_PLAYER_ID_KEY]
            ?.toIntOrNull()
            ?: return null
        return States(
            prev = prevId.takeIf { it != NIL_ID },
            next = nextId.takeIf { it != NIL_ID }
        )
    }

fun playerStateChangedAction(id: PID, states: States<PlayerState>) = GameAction(
    type = CHANGE_PLAYER_STATE,
    data = mapOf(
        PLAYER_ID_KEY to id.toString(),
        OLD_STATE_KEY to states.prev.toJson(),
        NEW_STATE_KEY to states.next.toJson()
    )
)

val GameAction.changesPlayerState: Boolean
    get() = type == CHANGE_PLAYER_STATE

val GameAction.playerId: PID?
    get() = data[PLAYER_ID_KEY]?.toIntOrNull()

val GameAction.playerStates: States<PlayerState>?
    get() {
        val prevState = data[OLD_STATE_KEY]
            ?.let { PlayerState.fromJson(it) }
            ?: return null
        val nextState = data[NEW_STATE_KEY]
            ?.let { PlayerState.fromJson(it) }
            ?: return null
        return States(
            prev = prevState,
            next = nextState
        )
    }

private const val CHANGE_CURRENT_PLAYER  = "change_current_player"
private const val OLD_PLAYER_ID_KEY = "old_player_id"
private const val NEW_PLAYER_ID_KEY = "new_player_id"
private const val NIL_ID: PID = -1

private const val CHANGE_PLAYER_STATE  = "change_player_state"
private const val PLAYER_ID_KEY = "player_id"
private const val OLD_STATE_KEY = "old_state"
private const val NEW_STATE_KEY = "new_state"
