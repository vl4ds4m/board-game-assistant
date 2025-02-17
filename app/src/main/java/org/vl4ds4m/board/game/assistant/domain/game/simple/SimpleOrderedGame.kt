package org.vl4ds4m.board.game.assistant.domain.game.simple

import org.vl4ds4m.board.game.assistant.domain.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

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