package org.vl4ds4m.board.game.assistant.domain.game.simple

import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.game.Game
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.OrderedGame
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.Ordered
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class SimpleOrderedGame(
    private val gameEnv: OrderedGameEnv = BaseOrderedGameEnv()
) : OrderedGame, Game by SimpleGame(gameEnv), Ordered by gameEnv {
    override fun saveIn(session: GameSession) {
        gameEnv.saveIn(session)
        session.type = GameType.ORDERED
    }

    fun addPoints(points: Int) {
        order.value?.let {
            players.value[it]
        }?.let {
            changePlayerScore(it, points)
            nextOrder()
        }
    }
}