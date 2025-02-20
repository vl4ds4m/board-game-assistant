package org.vl4ds4m.board.game.assistant.game.simple

import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.env.BaseGameEnv
import org.vl4ds4m.board.game.assistant.game.env.GameEnv

class FreeGame(gameEnv: GameEnv = BaseGameEnv(Free)) : GameEnv by gameEnv {
    private val scoreAdder = SimpleScoreAdder(gameEnv)

    fun addPoints(points: Int) {
        val id = currentPlayerId.value ?: return
        scoreAdder.addPoints(id, points)
    }
}
