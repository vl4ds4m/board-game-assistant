package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class DiceGame(gameEnv: OrderedGameEnv = BaseOrderedGameEnv(Dice)) : OrderedGameEnv by gameEnv {
    fun addPoints(points: Int) {
        if (points < 0 || points % 5 != 0) {
            return
        }
        val player = currentPlayer ?: return
        val oldScore = player.score
        if (oldScore == 1000) {
            return
        }
        val newScore = (oldScore + points).let {
            if (it > 1000) { oldScore - 100 }
            else { it }
        }
        val id = currentPlayerId.value ?: return
        changePlayerScore(id, newScore)
        selectNextPlayerId()
    }
}

/*internal fun isPlayerInHole(score: Int): Boolean {
    return score in (200 ..< 300) || score in (600 ..< 700)
}*/
