package org.vl4ds4m.board.game.assistant.domain.game.env

import org.vl4ds4m.board.game.assistant.data.GameSession

interface GameState {
    fun saveIn(session: GameSession)

    fun loadFrom(session: GameSession)
}
