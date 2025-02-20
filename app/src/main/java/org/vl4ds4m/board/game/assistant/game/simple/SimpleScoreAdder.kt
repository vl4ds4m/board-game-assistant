package org.vl4ds4m.board.game.assistant.game.simple

import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.state.Score
import kotlin.math.max

class SimpleScoreAdder(private val game: Game) {
    fun addPoints(playerId: Long, points: Int) {
        val score = game.players.value[playerId]?.state?.score ?: return
        val sum = max(score + points, 0)
        game.changePlayerState(playerId, Score(sum))
    }
}
