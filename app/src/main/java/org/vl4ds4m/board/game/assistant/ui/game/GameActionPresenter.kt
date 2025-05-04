package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.runtime.Composable
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.changesCurrentPlayer
import org.vl4ds4m.board.game.assistant.game.changesPlayerState
import org.vl4ds4m.board.game.assistant.game.currentPlayerIds
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.playerId
import org.vl4ds4m.board.game.assistant.game.playerStates

typealias ActionLog = @Composable (action: GameAction, players: Players) -> String

interface GameActionPresenter {
    val actionLog: ActionLog

    fun getPlayerName(players: Players, id: Long?): String {
        id ?: return "[nobody]"
        return players[id]?.name ?: "[removed]"
    }

    val fallback: String
        get() = "Unknown event"

    companion object : GameActionPresenter by BaseGameActionPresenter()
}

private class BaseGameActionPresenter : GameActionPresenter {
    override val actionLog: ActionLog = f@{ action, players ->
        when {
            action.changesCurrentPlayer -> {
                val ids = action.currentPlayerIds ?: return@f fallback
                val prevPlayer = getPlayerName(players, ids.prev)
                val nextPlayer = getPlayerName(players, ids.next)
                return@f "$prevPlayer -> $nextPlayer"
            }
            action.changesPlayerState -> {
                val playerId = action.playerId ?: return@f fallback
                val name = getPlayerName(players, playerId)
                val states = action.playerStates ?: return@f fallback
                val changes = states.run {
                    next.score - prev.score
                }
                return@f "$name: " + if (changes == 0) {
                    "no score changes"
                } else if (changes > 0) {
                    "+$changes point(s)"
                } else {
                    "$changes point(s)"
                }
            }
            else -> return@f fallback
        }
    }
}
