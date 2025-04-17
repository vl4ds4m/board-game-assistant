package org.vl4ds4m.board.game.assistant.game.simple

import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.Game
import org.vl4ds4m.board.game.assistant.game.env.GameEnv

interface FreeGame : Game {
    fun addPoints(points: Int)
}

class FreeGameEnv : GameEnv(Free), FreeGame {
    private val scoreAdder = SimpleScoreAdder(this)

    override fun addPoints(points: Int) {
        val id = currentPlayerId.value ?: return
        scoreAdder.addPoints(id, points)
    }
}
