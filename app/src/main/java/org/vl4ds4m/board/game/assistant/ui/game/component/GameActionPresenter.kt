package org.vl4ds4m.board.game.assistant.ui.game.component

import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.changesCurrentPlayer
import org.vl4ds4m.board.game.assistant.game.changesPlayerState
import org.vl4ds4m.board.game.assistant.game.currentPlayerIds
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.playerId
import org.vl4ds4m.board.game.assistant.game.playerStates

interface GameActionPresenter {
    fun showAction(action: GameAction, players: Players): String

    fun getPlayerName(players: Players, id: Long?): String {
        id ?: return "[nobody]"
        return players[id]?.name ?: "[removed]"
    }

    val fallback: String
        get() = "Unknown event"

    companion object {
        val Default: GameActionPresenter = BaseGameActionPresenter()
    }
}

private class BaseGameActionPresenter : GameActionPresenter {
    override fun showAction(action: GameAction, players: Players): String {
        when {
            action.changesCurrentPlayer -> {
                val ids = action.currentPlayerIds ?: return fallback
                val prevPlayer = getPlayerName(players, ids.prev)
                val nextPlayer = getPlayerName(players, ids.next)
                return "$prevPlayer -> $nextPlayer"
            }
            action.changesPlayerState -> {
                val playerId = action.playerId ?: return fallback
                val name = getPlayerName(players, playerId)
                val states = action.playerStates ?: return fallback
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
            else -> return fallback
        }
    }
}
