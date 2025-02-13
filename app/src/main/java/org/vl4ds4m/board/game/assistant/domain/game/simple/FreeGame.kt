package org.vl4ds4m.board.game.assistant.domain.game.simple

import org.vl4ds4m.board.game.assistant.domain.game.Free
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv

class FreeGame(
    private val gameEnv: GameEnv = BaseGameEnv(Free)
) : GameEnv by SimpleGame(gameEnv)
{
    fun addPoints(playerId: Long, points: Int) {
        players.value.find { it.id == playerId }
            ?.let { changePlayerScore(it, points) }
    }
}
