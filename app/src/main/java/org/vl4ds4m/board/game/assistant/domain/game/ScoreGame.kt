package org.vl4ds4m.board.game.assistant.domain.game

import kotlinx.coroutines.flow.MutableStateFlow
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.game.env.GameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import org.vl4ds4m.board.game.assistant.domain.player.state.holder.PlayerStatesHolder
import org.vl4ds4m.board.game.assistant.domain.player.state.holder.ScoresHolder
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.PlayerStateResolver
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.SimpleScoreResolver

abstract class ScoreGame(
    private val gameEnv: GameEnv,
    playerScores: MutableStateFlow<Map<Long, Score>>,
    private val scoresHolder: ScoresHolder
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

    constructor(gameEnv: GameEnv) : this(
        gameEnv = gameEnv,
        playerScores = MutableStateFlow(mapOf())
    )

    override val initializables: Array<Initializable> = arrayOf(scoresHolder)

    override fun saveIn(session: GameSession) {
        gameEnv.saveIn(session)
        scoresHolder.saveIn(session)
    }

    override fun loadFrom(session: GameSession) {
        gameEnv.loadFrom(session)
        scoresHolder.loadFrom(session)
    }
}
