package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv

interface Game : GameEnv {
    val initializables: Array<Initializable>
}
