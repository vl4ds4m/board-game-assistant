package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.ui.res.stringResource
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.changesPlayerState
import org.vl4ds4m.board.game.assistant.game.monopoly.inPrison
import org.vl4ds4m.board.game.assistant.game.monopoly.position
import org.vl4ds4m.board.game.assistant.game.playerId
import org.vl4ds4m.board.game.assistant.game.playerStates
import org.vl4ds4m.board.game.assistant.ui.game.ActionLog
import org.vl4ds4m.board.game.assistant.ui.game.GameActionPresenter

class MonopolyGameActionPresenter private constructor(
    private val default: GameActionPresenter
) : GameActionPresenter {
    override val actionLog: ActionLog = f@{ action, players ->
        if (action.changesPlayerState) {
            val playerId = action.playerId ?: return@f fallback
            val name = getPlayerName(players, playerId)
            val states = action.playerStates ?: return@f fallback
            states.run {
                val prevPos = prev.position ?: return@f fallback
                val nextPos = next.position ?: return@f fallback
                if (prevPos != nextPos)
                    return@f "$name: ${monopolyField(prevPos)} -> ${monopolyField(nextPos)}"
                val prevInPrison = prev.inPrison ?: return@f fallback
                val nextInPrison = next.inPrison ?: return@f fallback
                if (prevInPrison != nextInPrison) {
                    return@f "$name " +
                        if (nextInPrison) stringResource(R.string.game_log_monopoly_to_prison)
                        else stringResource(R.string.game_log_monopoly_from_prison)
                }
            }
        }
        return@f default.actionLog(action, players)
    }

    companion object : GameActionPresenter by MonopolyGameActionPresenter(GameActionPresenter)
}
