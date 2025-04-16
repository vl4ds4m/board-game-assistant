package org.vl4ds4m.board.game.assistant.game.carcassonne

import org.vl4ds4m.board.game.assistant.game.Carcassonne
import org.vl4ds4m.board.game.assistant.game.data.Score
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv

class CarcassonneGame : OrderedGameEnv(Carcassonne) {
    fun addPoints(property: CarcassonneProperty, count: Int, final: Boolean) {
        if (count <= 0) return
        var points = 0
        when (property) {
            CarcassonneProperty.CLOISTER -> if (count <= 9) points = count
            CarcassonneProperty.FIELD -> if (final) points = 3 * count
            CarcassonneProperty.CITY -> points = if (final) count else 2 * count
            CarcassonneProperty.ROAD -> points = count
        }
        if (points > 0) {
            val (_, player) = currentPlayer ?: return
            val score = Score(player.state.score + points)
            val id = currentPlayerId.value ?: return
            changePlayerState(id, score)
        }
    }
}
