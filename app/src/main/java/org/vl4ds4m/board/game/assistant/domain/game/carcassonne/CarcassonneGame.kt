package org.vl4ds4m.board.game.assistant.domain.game.carcassonne

import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv

class CarcassonneGame(
    private val gameEnv: OrderedGameEnv = BaseOrderedGameEnv(GameType.CARCASSONNE)
) : OrderedGameEnv by gameEnv
