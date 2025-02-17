package org.vl4ds4m.board.game.assistant.domain.game.simple

import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import kotlin.math.max

class SimpleScoreAdder(private val gameEnv: GameEnv) {
    fun addPoints(playerId: Long, points: Int) {
        val score = gameEnv.players.value[playerId]?.score ?: return
        val sum = max(score + points, 0)
        gameEnv.changePlayerScore(playerId, sum)
    }
}
