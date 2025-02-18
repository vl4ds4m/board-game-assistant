package org.vl4ds4m.board.game.assistant.domain.game.carcassonne

import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.game.Carcassonne
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class CarcassonneGame(
    private val gameEnv: OrderedGameEnv = BaseOrderedGameEnv(Carcassonne)
) : OrderedGameEnv by gameEnv
{
    val onFinal: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun loadFrom(session: GameSession) {
        gameEnv.loadFrom(session)
        session.state.let {
            it as? CarcassonneGameState
        }?.let {
            this.onFinal.value = it.onFinal
        }
    }

    override fun saveIn(session: GameSession) {
        session.state = session.state.let {
            it as? CarcassonneGameState ?: CarcassonneGameState()
        }.also {
            it.onFinal = this.onFinal.value
        }
        gameEnv.saveIn(session)
    }

    fun addPoints(property: CarcassonneProperty, count: Int) {
        if (count <= 0) return
        var points = 0
        when (property) {
            CarcassonneProperty.CLOISTER -> if (count <= 9) points = count
            CarcassonneProperty.FIELD -> if (onFinal.value) points = 3 * count
            CarcassonneProperty.CITY -> points = if (onFinal.value) count else 2 * count
            CarcassonneProperty.ROAD -> points = count
        }
        if (points > 0) {
            val player = currentPlayer ?: return
            val score = player.score + points
            val id = currentPlayerId.value ?: return
            gameEnv.changePlayerScore(id, score)
            gameEnv.selectNextPlayerId()
        }
    }
}
