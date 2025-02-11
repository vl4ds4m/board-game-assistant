package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.Score

class FreeGame private constructor(
    gameEnv: GameEnv
) : ScoreGame(gameEnv) {
    constructor() : this(BaseGameEnv())

    override fun saveIn(session: GameSession) {
        super.saveIn(session)
        session.type = GameType.FREE
    }

    fun addPoints(playerId: Long, points: Int) {
        resolve(playerId, Score(points))
    }
}
