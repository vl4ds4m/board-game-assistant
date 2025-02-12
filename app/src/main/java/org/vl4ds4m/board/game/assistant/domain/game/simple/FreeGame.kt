package org.vl4ds4m.board.game.assistant.domain.game.simple

import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.game.Game
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv

class FreeGame(
    private val gameEnv: GameEnv = BaseGameEnv()
) : Game by SimpleGame(gameEnv) {
    override fun saveIn(session: GameSession) {
        gameEnv.saveIn(session)
        session.type = GameType.FREE
    }

    fun addPoints(playerId: Long, points: Int) {
        players.value.find { it.id == playerId }
            ?.let { changePlayerScore(it, points) }
    }
}
