package org.vl4ds4m.board.game.assistant.game.simple

import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv

class SimpleOrderedGame : OrderedGameEnv(SimpleOrdered) {
    private val scoreAdder = SimpleScoreAdder(this)

    fun addPoints(points: Int) {
        val id = currentPlayerId.value ?: return
        scoreAdder.addPoints(id, points)
        changeCurrentPlayerId()
    }
}