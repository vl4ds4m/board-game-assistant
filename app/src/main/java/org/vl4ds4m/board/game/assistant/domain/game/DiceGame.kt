package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class DiceGame(
    private val gameEnv: OrderedGameEnv = BaseOrderedGameEnv()
) : OrderedGame, OrderedGameEnv by gameEnv {
    override val initializables: Array<Initializable> = arrayOf()

    override fun saveIn(session: GameSession) {
        gameEnv.saveIn(session)
        session.type = GameType.DICE
    }

    fun addPoints(points: Int) {
        if (points < 0 || points % 5 != 0) {
            return
        }
        val player = order.value?.let { players.value[it] } ?: return
        val oldScore = player.score
        if (oldScore == 1000) {
            return
        }
        val newScore = (oldScore + points).let {
            if (it > 1000) { oldScore - 100 }
            else { it }
        }
        changePlayerScore(player, newScore)
        nextOrder()
    }
}

/*internal fun isPlayerInHole(score: Int): Boolean {
    return score in (200 ..< 300) || score in (600 ..< 700)
}*/
