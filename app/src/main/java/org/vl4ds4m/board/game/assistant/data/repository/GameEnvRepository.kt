package org.vl4ds4m.board.game.assistant.data.repository

import org.vl4ds4m.board.game.assistant.game.env.GameEnv

class GameEnvRepository {
    private var instance: GameEnv? = null

    fun put(gameEnv: GameEnv) {
        instance = gameEnv
    }

    fun extract(): GameEnv {
        return instance ?: throw IllegalStateException("GameEnv doesn't exist")
    }
}
