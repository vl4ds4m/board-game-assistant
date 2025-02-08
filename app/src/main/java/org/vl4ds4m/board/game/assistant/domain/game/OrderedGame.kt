package org.vl4ds4m.board.game.assistant.domain.game

import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import org.vl4ds4m.board.game.assistant.domain.player.state.holder.PlayerStatesHolder
import org.vl4ds4m.board.game.assistant.domain.player.state.holder.ScoresHolder
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.PlayerStateResolver
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.SimpleScoreResolver

class OrderedGame private constructor(
    gameEnv: OrderedGameEnv,
    playerScores: MutableStateFlow<Map<Long, Score>>,
    private val scoresHolder: ScoresHolder
) : Game<Score>,
    OrderedGameEnv by gameEnv,
    PlayerStatesHolder<Score> by scoresHolder,
    PlayerStateResolver<Score> by SimpleScoreResolver(playerScores)
{
    constructor(
        gameEnv: OrderedGameEnv,
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
        gameEnv = BaseOrderedGameEnv(),
        playerScores = MutableStateFlow(mapOf())
    )

    override val initializables: Array<Initializable> = arrayOf(scoresHolder)

    fun addPoints(points: Int) {
        val score = Score(points)
        val playerId = order.value?.let {
            players.value[it].id
        } ?: -1
        resolve(playerId, score)
        nextOrder()
    }
}
