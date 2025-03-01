package org.vl4ds4m.board.game.assistant.data.repository

import org.vl4ds4m.board.game.assistant.game.Game

class GameRepository {
    private var instance: Game? = null

    fun put(game: Game) {
        instance = game
    }

    fun extract(): Game {
        return instance?.also {
            instance = null
        } ?: throw IllegalStateException("Game doesn't exist")
    }
}
