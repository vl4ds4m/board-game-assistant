package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.Ordered
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class OrderedGame(
    gameEnv: OrderedGameEnv = BaseOrderedGameEnv()
) : SimpleGame(gameEnv), Ordered by gameEnv {
    override fun saveIn(session: GameSession) {
        super.saveIn(session)
        session.type = GameType.ORDERED
    }

    fun addPoints(points: Int) {
        order.value?.let {
            players.value[it]
        }?.let {
            changeScore(it, points)
            nextOrder()
        }
    }
}
