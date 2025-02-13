package org.vl4ds4m.board.game.assistant.domain.game.simple

import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.Ordered
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class SimpleOrderedGame(
    private val gameEnv: OrderedGameEnv = BaseOrderedGameEnv(GameType.ORDERED)
) : OrderedGameEnv,
    GameEnv by SimpleGame(gameEnv),
    Ordered by gameEnv
{
    fun addPoints(points: Int) {
        order.value?.let {
            players.value[it]
        }?.let {
            changePlayerScore(it, points)
            nextOrder()
        }
    }
}