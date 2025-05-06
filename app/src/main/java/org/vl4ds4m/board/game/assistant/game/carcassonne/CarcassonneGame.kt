package org.vl4ds4m.board.game.assistant.game.carcassonne

import org.vl4ds4m.board.game.assistant.game.Carcassonne
import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv
import kotlin.math.min

interface CarcassonneGame : OrderedGame {
    fun addPoints(property: CarcassonneProperty, count: Int, final: Boolean)
}

class CarcassonneGameEnv : OrderedGameEnv(Carcassonne), CarcassonneGame {
    override fun addPoints(property: CarcassonneProperty, count: Int, final: Boolean) {
        if (count !in 1 .. MAX_SCORE) return
        var points = 0
        when (property) {
            CarcassonneProperty.CLOISTER -> if (count <= 9) points = count
            CarcassonneProperty.FIELD -> if (final) points = 3 * count
            CarcassonneProperty.CITY -> points = if (final) count else 2 * count
            CarcassonneProperty.ROAD -> points = count
        }
        if (points > 0) {
            val (_, player) = currentPlayer ?: return
            val score = player.state.run {
                copy(score = min(score + points, MAX_SCORE))
            }
            val id = currentPid.value ?: return
            changePlayerState(id, score)
        }
    }
}

private const val MAX_SCORE = 999_999
