package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseGameEnv

class FreeGame : SimpleGame(BaseGameEnv()) {
    override fun saveIn(session: GameSession) {
        super.saveIn(session)
        session.type = GameType.FREE
    }

    fun addPoints(playerId: Long, points: Int) {
        players.value.find { it.id == playerId }
            ?.let { changeScore(it, points) }
    }
}
