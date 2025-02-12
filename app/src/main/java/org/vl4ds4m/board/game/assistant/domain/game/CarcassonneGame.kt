package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class CarcassonneGame(
    private val gameEnv: OrderedGameEnv = BaseOrderedGameEnv()
) : OrderedGame, OrderedGameEnv by gameEnv {
    override val initializables: Array<Initializable> = arrayOf()

    override fun saveIn(session: GameSession) {
        gameEnv.saveIn(session)
        session.type = GameType.CARCASSONNE
    }
}
