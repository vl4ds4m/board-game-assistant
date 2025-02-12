package org.vl4ds4m.board.game.assistant.domain.game

import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.player.Player
import kotlin.math.max

abstract class SimpleGame(gameEnv: GameEnv) : Game, GameEnv by gameEnv {
    override val initializables: Array<Initializable> = arrayOf()

    protected fun changeScore(player: Player, points: Int) {
        val newScore = max(player.score + points, 0)
        changePlayerScore(player, newScore)
    }
}
