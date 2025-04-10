package org.vl4ds4m.board.game.assistant.game.carcassonne

import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.game.Carcassonne
import org.vl4ds4m.board.game.assistant.game.data.CarcassonneGameState
import org.vl4ds4m.board.game.assistant.game.data.GameState
import org.vl4ds4m.board.game.assistant.game.data.Score
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv

class CarcassonneGame : OrderedGameEnv(Carcassonne) {
    val finalStage: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun restoreAdditionalState(state: GameState?) {
        super.restoreAdditionalState(state)
        state.let {
            it as? CarcassonneGameState
        }?.let {
            finalStage.value = it.finalStage
        }
    }

    override val additionalState
        get() = CarcassonneGameState(super.additionalState, finalStage.value)

    fun addPoints(property: CarcassonneProperty, count: Int) {
        if (count <= 0) return
        var points = 0
        when (property) {
            CarcassonneProperty.CLOISTER -> if (count <= 9) points = count
            CarcassonneProperty.FIELD -> if (finalStage.value) points = 3 * count
            CarcassonneProperty.CITY -> points = if (finalStage.value) count else 2 * count
            CarcassonneProperty.ROAD -> points = count
        }
        if (points > 0) {
            val (_, player) = currentPlayer ?: return
            val score = Score(player.state.score + points)
            val id = currentPlayerId.value ?: return
            changePlayerState(id, score)
            changeCurrentPlayerId()
        }
    }
}
