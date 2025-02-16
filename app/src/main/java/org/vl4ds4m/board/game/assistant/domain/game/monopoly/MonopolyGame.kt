package org.vl4ds4m.board.game.assistant.domain.game.monopoly

import org.vl4ds4m.board.game.assistant.domain.game.Monopoly
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class MonopolyGame(
    private val gameEnv: OrderedGameEnv = BaseOrderedGameEnv(Monopoly)
) : OrderedGameEnv by gameEnv
{

}
