package org.vl4ds4m.board.game.assistant.game.simple

import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv

class SimpleOrderedGame(
    gameEnv: OrderedGameEnv = BaseOrderedGameEnv(SimpleOrdered)
) : OrderedGameEnv by gameEnv
{
    private val scoreAdder = SimpleScoreAdder(gameEnv)

    fun addPoints(points: Int) {
        val id = currentPlayerId.value ?: return
        scoreAdder.addPoints(id, points)
        selectNextPlayerId()
    }
}