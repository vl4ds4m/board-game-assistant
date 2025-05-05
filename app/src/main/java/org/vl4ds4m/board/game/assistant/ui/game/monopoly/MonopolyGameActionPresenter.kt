package org.vl4ds4m.board.game.assistant.ui.game.monopoly

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.vl4ds4m.board.game.assistant.R
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.changesPlayerState
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.game.monopoly.changesMonopolyPlayersCash
import org.vl4ds4m.board.game.assistant.game.monopoly.inPrison
import org.vl4ds4m.board.game.assistant.game.monopoly.monopolyAmount
import org.vl4ds4m.board.game.assistant.game.monopoly.position
import org.vl4ds4m.board.game.assistant.game.monopoly.receiverId
import org.vl4ds4m.board.game.assistant.game.monopoly.senderId
import org.vl4ds4m.board.game.assistant.game.playerId
import org.vl4ds4m.board.game.assistant.game.playerStates
import org.vl4ds4m.board.game.assistant.ui.game.ActionLog
import org.vl4ds4m.board.game.assistant.ui.game.GameActionPresenter

class MonopolyGameActionPresenter private constructor(
    private val default: GameActionPresenter
) : GameActionPresenter {
    override val actionLog: ActionLog = { action, players ->
        val log = when {
            action.changesMonopolyPlayersCash -> {
                logPlayersCashChanged(action, players)
            }
            action.changesPlayerState -> {
                logPlayerStateChanged(action, players)
            }
            else -> default.actionLog(action, players)
        }
        log ?: fallback
    }

    private fun logPlayersCashChanged(action: GameAction, players: Players): String? {
        val sender = action.senderId
            ?.let { getPlayerName(players, it) }
            ?: return null
        val receiver = action.receiverId
            ?.let { getPlayerName(players, it) }
            ?: return null
        val amount = action.monopolyAmount
            ?: return null
        return "$sender -> $receiver: $amount y.e."
    }

    @Composable
    private fun logPlayerStateChanged(action: GameAction, players: Players): String? {
        val player = action.playerId
            ?.let { getPlayerName(players, it) }
            ?: return null
        val states = action.playerStates
            ?: return null
        val logs = mutableListOf<String>()
        states.run {
            val prevPos = prev.position ?: return null
            val nextPos = next.position ?: return null
            if (prevPos != nextPos) {
                logs += "$player: ${monopolyField(prevPos)} -> ${monopolyField(nextPos)}"
            }
            val prevInPrison = prev.inPrison ?: return null
            val nextInPrison = next.inPrison ?: return null
            if (prevInPrison != nextInPrison) {
                val log = "$player " +
                    if (nextInPrison) stringResource(R.string.game_log_monopoly_to_prison)
                    else stringResource(R.string.game_log_monopoly_from_prison)
                logs += log
            }
            val moneyChanges = next.score - prev.score
            if (moneyChanges != 0) {
                val log = "$player: " +
                    (if (moneyChanges > 0) "+" else "") +
                    "$moneyChanges y.e."
                logs += log
            }
        }
        return if (logs.isNotEmpty()) {
            val builder = StringBuilder(logs[0])
            for (i in 1 .. logs.lastIndex) {
                builder.appendLine()
                builder.append(logs[i])
            }
            builder.toString()
        } else null
    }

    companion object : GameActionPresenter by MonopolyGameActionPresenter(GameActionPresenter)
}
