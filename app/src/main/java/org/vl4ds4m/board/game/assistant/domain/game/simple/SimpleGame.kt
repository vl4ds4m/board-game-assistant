package org.vl4ds4m.board.game.assistant.domain.game.simple

import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.Game
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.player.Player
import kotlin.math.max

class SimpleGame(private val gameEnv: GameEnv) : Game, GameEnv by gameEnv {
    override val initializables: Array<Initializable> = arrayOf()

    override fun changePlayerScore(player: Player, score: Int) {
        val newScore = max(player.score + score, 0)
        gameEnv.changePlayerScore(player, newScore)
    }
}
