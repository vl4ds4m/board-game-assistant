package org.vl4ds4m.board.game.assistant.domain.player.state.resolver

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.domain.player.state.Score
import kotlin.math.max

class SimpleScoreResolver(
    private val playerScores: MutableStateFlow<Map<Long, Score>>
) : PlayerStateResolver<Score> {
    override fun resolve(playerId: Long, newState: Score) {
        if (
            newState.value == 0 ||
            playerId !in playerScores.value
        ) {
            return
        }
        playerScores.update {
            buildMap {
                putAll(it)
                val oldValue = get(playerId)!!.value
                val newValue = max(oldValue + newState.value, 0)
                put(playerId, Score(newValue))
            }
        }
    }
}
