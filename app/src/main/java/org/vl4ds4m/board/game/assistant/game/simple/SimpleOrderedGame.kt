package org.vl4ds4m.board.game.assistant.game.simple

import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv

interface SimpleOrderedGame : OrderedGame {
    fun addPoints(points: Int)
}

class SimpleOrderedGameEnv : OrderedGameEnv(SimpleOrdered), SimpleOrderedGame {
    private val scoreAdder = SimpleScoreAdder(this)

    override fun addPoints(points: Int) {
        val id = currentPlayerId.value ?: return
        scoreAdder.addPoints(id, points)
    }
}
