package org.vl4ds4m.board.game.assistant.game

import org.vl4ds4m.board.game.assistant.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.game.data.Score

class DiceGame : BaseOrderedGameEnv(Dice) {
    fun addPoints(points: Int) {
        if (points < 0 || points % 5 != 0) {
            return
        }
        val player = currentPlayer ?: return
        val oldScore = player.state.score
        if (oldScore == 1000) {
            return
        }
        val newScore = (oldScore + points).let {
            if (it > 1000) oldScore - 100
            else it
        }.let {
            Score(it)
        }
        val id = currentPlayerId.value ?: return
        changePlayerState(id, newScore)
        changeCurrentPlayerId()
    }
}

/*internal fun isPlayerInHole(score: Int): Boolean {
    return score in (200 ..< 300) || score in (600 ..< 700)
}*/
