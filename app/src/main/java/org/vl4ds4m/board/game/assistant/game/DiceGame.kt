package org.vl4ds4m.board.game.assistant.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.game.env.Initializable

interface DiceGame : OrderedGame {
    val addEnabled: StateFlow<Boolean>

    fun addPoints(points: Int)
}

class DiceGameEnv : OrderedGameEnv(Dice), DiceGame {
    private val mAddEnabled = MutableStateFlow(true)
    override val addEnabled: StateFlow<Boolean> = mAddEnabled.asStateFlow()

    private val addEnabledObserver = Initializable { scope ->
        currentPid.combine(actions) { id, actions ->
            mAddEnabled.value = actions.takeIf { id != null }
                ?.takeIf { actions.isNotEmpty() }
                ?.last()
                ?.takeIf { it.changesPlayerState }
                ?.takeIf { it.playerId == id }
                ?.let { it.playerStates?.prev?.score }
                ?.let { !isPlayerInHole(it) }
                ?: true
            }.launchIn(scope)
    }

    override val initializables = super.initializables + addEnabledObserver

    override fun addPoints(points: Int) {
        if ((points !in 1 .. 1000) || points % 5 != 0) return
        val (_, player) = currentPlayer ?: return
        val oldScore = player.state.score
        if (oldScore == 1000) return
        val newScore = (oldScore + points).let {
            if (it > 1000) oldScore - 100
            else it
        }.let {
            player.state.copy(score = it)
        }
        val id = currentPid.value ?: return
        changePlayerState(id, newScore)
    }
}

private fun isPlayerInHole(score: Int): Boolean {
    return score in (200 ..< 300) || score in (600 ..< 700)
}
