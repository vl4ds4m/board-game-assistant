package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class MonopolyGame(
    private val gameEnv: OrderedGameEnv = BaseOrderedGameEnv(GameType.MONOPOLY)
) : OrderedGameEnv by gameEnv
