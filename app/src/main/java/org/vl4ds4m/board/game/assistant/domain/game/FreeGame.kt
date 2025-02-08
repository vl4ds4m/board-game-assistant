package org.vl4ds4m.board.game.assistant.domain.game

import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import org.vl4ds4m.board.game.assistant.domain.player.state.holder.PlayerStatesHolder
import org.vl4ds4m.board.game.assistant.domain.player.state.holder.ScoresHolder
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.PlayerStateResolver
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.SimpleScoreResolver

class FreeGame private constructor(
    gameEnv: GameEnv,
    playerScores: MutableStateFlow<Map<Long, Score>>,
    scoresHolder: ScoresHolder
) : Game<Score>, GameEnv by gameEnv,
    PlayerStatesHolder<Score> by scoresHolder,
    PlayerStateResolver<Score> by SimpleScoreResolver(playerScores)
{
    constructor(
        gameEnv: GameEnv,
        playerScores: MutableStateFlow<Map<Long, Score>>
    ) : this(
        gameEnv = gameEnv,
        playerScores = playerScores,
        scoresHolder = ScoresHolder(
            players = gameEnv.players,
            playerScores = playerScores
        )
    )

    constructor() : this(
        gameEnv = BaseGameEnv(),
        playerScores = MutableStateFlow(mapOf())
    )

    override val initializables: Array<Initializable> = arrayOf(scoresHolder)

    fun addPoints(playerId: Long, points: Int) {
        resolve(playerId, Score(points))
    }
}
