package org.vl4ds4m.board.game.assistant.game

import org.vl4ds4m.board.game.assistant.States
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.log.GameAction

fun currentPlayerChangedAction(ids: States<Long?>) = GameAction(
    type = CHANGE_CURRENT_PLAYER,
    data = mapOf(
        OLD_PLAYER_ID_KEY to ids.prev.let { it ?: -1L }.toString(),
        NEW_PLAYER_ID_KEY to ids.next.let { it ?: -1L }.toString()
    )
)

val GameAction.changesCurrentPlayer: Boolean
    get() = type == CHANGE_CURRENT_PLAYER

val GameAction.currentPlayerIds: States<Long?>?
    get() {
        val prevId = data[OLD_PLAYER_ID_KEY]
            ?.toLongOrNull()
            ?: return null
        val nextId = data[NEW_PLAYER_ID_KEY]
            ?.toLongOrNull()
            ?: return null
        return States(
            prev = prevId.takeIf { it != -1L },
            next = nextId.takeIf { it != -1L }
        )
    }

fun playerStateChangedAction(id: Long, states: States<PlayerState>) = GameAction(
    type = CHANGE_PLAYER_STATE,
    data = mapOf(
        PLAYER_ID_KEY to id.toString(),
        OLD_STATE_KEY to states.prev.toJson(),
        NEW_STATE_KEY to states.next.toJson()
    )
)

val GameAction.changesPlayerState: Boolean
    get() = type == CHANGE_PLAYER_STATE

val GameAction.playerId: Long?
    get() = data[PLAYER_ID_KEY]?.toLongOrNull()

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

private const val CHANGE_PLAYER_STATE  = "change_player_state"
private const val PLAYER_ID_KEY = "player_id"
private const val OLD_STATE_KEY = "old_state"
private const val NEW_STATE_KEY = "new_state"
