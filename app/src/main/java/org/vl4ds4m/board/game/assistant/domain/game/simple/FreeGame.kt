package org.vl4ds4m.board.game.assistant.domain.game.simple

import org.vl4ds4m.board.game.assistant.domain.game.Free
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv

class FreeGame(gameEnv: GameEnv = BaseGameEnv(Free)) : GameEnv by gameEnv {
    private val scoreAdder = SimpleScoreAdder(gameEnv)

    fun addPoints(points: Int) {
        val id = currentPlayerId.value ?: return
        scoreAdder.addPoints(id, points)
    }
}
