package org.vl4ds4m.board.game.assistant.game.env

import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.Game

interface GameEnv : Game {
    val initializables: Array<Initializable>

    fun loadFrom(session: GameSession)

    fun saveIn(session: GameSession)

    companion object {
        const val TAG = "GameEnvironment"
    }
}
