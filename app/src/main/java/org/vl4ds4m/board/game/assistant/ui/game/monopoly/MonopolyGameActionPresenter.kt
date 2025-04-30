package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.changesPlayerState
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.monopoly.inPrison
import org.vl4ds4m.board.game.assistant.game.monopoly.position
import org.vl4ds4m.board.game.assistant.game.playerId
import org.vl4ds4m.board.game.assistant.game.playerStates
import org.vl4ds4m.board.game.assistant.ui.game.GameActionPresenter

class MonopolyGameActionPresenter private constructor(
    private val default: GameActionPresenter
) : GameActionPresenter {
    override fun showAction(action: GameAction, players: Players): String {
        if (action.changesPlayerState) {
            val playerId = action.playerId ?: return fallback
            val name = getPlayerName(players, playerId)
            val states = action.playerStates ?: return fallback
            states.run {
                val prevPos = prev.position ?: return fallback
                val nextPos = next.position ?: return fallback
                if (prevPos != nextPos) return "$name: $prevPos ---> $nextPos"
                val prevInPrison = prev.inPrison ?: return fallback
                val nextInPrison = next.inPrison ?: return fallback
                if (prevInPrison != nextInPrison) {
                    return "$name: " +
                        if (nextInPrison) "IN PRISON !"
                        else "released from prison"
                }
            }
        }
        return default.showAction(action, players)
    }

    companion object : GameActionPresenter by MonopolyGameActionPresenter(
        GameActionPresenter.Default
    )
}
