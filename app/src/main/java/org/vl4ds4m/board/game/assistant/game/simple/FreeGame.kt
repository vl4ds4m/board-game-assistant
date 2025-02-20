package org.vl4ds4m.board.game.assistant.game.simple

import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.env.BaseGameEnv

class FreeGame : BaseGameEnv(Free) {
    private val scoreAdder = SimpleScoreAdder(this)

    fun addPoints(points: Int) {
        val id = currentPlayerId.value ?: return
        scoreAdder.addPoints(id, points)
    }
}
