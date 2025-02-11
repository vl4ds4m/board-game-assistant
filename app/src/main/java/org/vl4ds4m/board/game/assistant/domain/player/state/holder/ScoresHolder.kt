package org.vl4ds4m.board.game.assistant.domain.player.state.holder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.Initializable
import org.vl4ds4m.board.game.assistant.domain.player.Player
import org.vl4ds4m.board.game.assistant.domain.player.state.Score

class ScoresHolder(
    private val players: StateFlow<List<Player>>,
    private val playerScores: MutableStateFlow<Map<Long, Score>>
) : PlayerStatesHolder<Score>, Initializable {
    override val playerStates: StateFlow<Map<Long, Score>> =
        playerScores.asStateFlow()

    override fun saveIn(session: GameSession) {
        session.playerStates = playerStates.value
    }

    @Suppress("UNCHECKED_CAST")
    override fun loadFrom(session: GameSession) {
        session.playerStates?.let {
            playerScores.value = it as Map<Long, Score>
        }
    }

    private var job: Job? = null

    override fun init(scope: CoroutineScope) {
        job?.cancel()
        val flow = players.onEach { ps ->
            playerScores.update { map ->
                buildMap {
                    for (p in ps) {
                        val id = p.id
                        val score = map[id] ?: Score(0)
                        put(id, score)
                    }
                }
            }
        }
        job = flow.launchIn(scope)
    }

    override fun close() {
        job?.cancel()
        job = null
    }
}
