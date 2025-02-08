package org.vl4ds4m.board.game.assistant.domain.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.domain.game.env.BaseOrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.PlayerStateResolver
import org.vl4ds4m.board.game.assistant.domain.player.state.resolver.SimpleScoreResolver

class OrderedGame private constructor(
    scope: CoroutineScope,
    gameEnv: BaseOrderedGameEnv,
    mPlayerScores: MutableStateFlow<Map<Long, Score>>
) : OrderedGameEnv by gameEnv,
    PlayerStateResolver<Score> by SimpleScoreResolver(mPlayerScores)
{
    constructor(scope: CoroutineScope) : this(
        scope = scope,
        gameEnv = BaseOrderedGameEnv(),
        mPlayerScores = MutableStateFlow(mapOf())
    )

    init {
        players.onEach { players ->
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
            .launchIn(scope)
    }

    val playerScores: StateFlow<Map<Long, Score>> =
        mPlayerScores.asStateFlow()

    fun addScore(score: Score) {
        resolve(players.value[order.value!!].id, score)
        nextOrder()
    }
}
