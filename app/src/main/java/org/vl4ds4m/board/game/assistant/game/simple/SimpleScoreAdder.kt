package org.vl4ds4m.board.game.assistant.game.simple

import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.PID
import kotlin.math.max

class SimpleScoreAdder(private val game: Game) {
    fun addPoints(playerId: PID, points: Int) {
        val state = game.players.value[playerId]?.state ?: return
        val sum = max(state.score + points, 0)
        game.changePlayerState(playerId, state.copy(score = sum))
    }
}
