package org.vl4ds4m.board.game.assistant.domain.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.PlayerStateResolver
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.SimpleScoreResolver

class OrderedGame private constructor(
    private val gameEnv: BaseOrderedGameEnv,
    private val mPlayerScores: MutableStateFlow<Map<Long, Score>>
) : OrderedGameEnv by gameEnv,
    PlayerStateResolver<Score> by SimpleScoreResolver(mPlayerScores)
{
    constructor() : this(
        gameEnv = BaseOrderedGameEnv(),
        mPlayerScores = MutableStateFlow(mapOf())
    )

    fun start(scope: CoroutineScope) {
        scope.launch {
            players.collect { players ->
                mPlayerScores.update { map ->
                    buildMap {
                        for (player in players) {
                            val id = player.id
                            val score = map[id] ?: Score(0)
                            put(id, score)
                        }
                    }
                }
            }
        }
    }

    val playerScores: StateFlow<Map<Long, Score>> =
        mPlayerScores.asStateFlow()

    fun addScore(score: Score) {
        resolve(players.value[order.value!!].id, score)
        nextOrder()
    }
}
