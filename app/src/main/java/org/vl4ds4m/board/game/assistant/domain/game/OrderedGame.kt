package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.Ordered
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.Score

class OrderedGame private constructor(
    gameEnv: OrderedGameEnv
) : ScoreGame(gameEnv), Ordered by gameEnv {
    constructor() : this(BaseOrderedGameEnv())

    override fun saveIn(session: GameSession) {
        super.saveIn(session)
        session.type = GameType.ORDERED
    }

    fun addPoints(points: Int) {
        val score = Score(points)
        val playerId = order.value?.let {
            players.value[it].id
        } ?: -1
        resolve(playerId, score)
        nextOrder()
    }
}
