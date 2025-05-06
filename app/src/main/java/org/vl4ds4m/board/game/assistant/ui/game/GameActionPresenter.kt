package org.vl4ds4m.board.game.assistant.ui.game

import androidx.compose.runtime.Composable
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.changesCurrentPlayer
import org.vl4ds4m.board.game.assistant.game.changesPlayerState
import org.vl4ds4m.board.game.assistant.game.currentPIDs
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.playerId
import org.vl4ds4m.board.game.assistant.game.playerStates

typealias ActionLog = @Composable (action: GameAction, players: Players) -> String

interface GameActionPresenter {
    val actionLog: ActionLog

    fun getPlayerName(players: Players, id: PID?): String {
        id ?: return "[nobody]"
        return players[id]?.name ?: "[removed]"
    }

    val fallback: String
        get() = "Unknown event"

    companion object : GameActionPresenter by BaseGameActionPresenter()
}

private class BaseGameActionPresenter : GameActionPresenter {
    override val actionLog: ActionLog = { action, players ->
        val log = when {
            action.changesCurrentPlayer -> {
                logCurrentPlayerChanged(action, players)
            }
            action.changesPlayerState -> {
                logPlayerStateChanged(action, players)
            }
            else -> null
        }
        log ?: fallback
    }

    private fun logCurrentPlayerChanged(action: GameAction, players: Players): String? {
        val ids = action.currentPIDs ?: return null
        val prevPlayer = getPlayerName(players, ids.prev)
        val nextPlayer = getPlayerName(players, ids.next)
        return "$prevPlayer -> $nextPlayer"
    }

    private fun logPlayerStateChanged(action: GameAction, players: Players): String? {
        val playerId = action.playerId ?: return null
        val name = getPlayerName(players, playerId)
        val states = action.playerStates ?: return null
        val changes = states.run {
            next.score - prev.score
        }
        return "$name: " + if (changes == 0) {
            "no score changes"
        } else if (changes > 0) {
            "+$changes point(s)"
        } else {
            "$changes point(s)"
        }
    }
}
