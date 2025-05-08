package org.vl4ds4m.board.game.assistant.game.simple

import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.PID
import kotlin.math.max
import kotlin.math.min

class SimpleScoreAdder(private val game: Game) {
    fun addPoints(playerId: PID, points: Int) {
        if (points == 0) return
        if (points !in -MAX_SCORE .. MAX_SCORE) return
        val state = game.players.value[playerId]?.state ?: return
        val sum = if (points > 0) {
            min(state.score + points, MAX_SCORE)
        } else {
            max(state.score + points, 0)
        }
        game.changePlayerState(playerId, state.copy(score = sum))
    }
}

private const val MAX_SCORE = 999_999_999
