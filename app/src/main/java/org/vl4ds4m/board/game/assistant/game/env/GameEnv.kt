package org.vl4ds4m.board.game.assistant.game.env

import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.game.Game

interface GameEnv : Game {
    val initializables: Array<Initializable>

    fun loadFrom(session: GameSession)

    fun saveIn(session: GameSession)
}
