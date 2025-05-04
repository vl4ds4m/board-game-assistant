package org.vl4ds4m.board.game.assistant.game.monopoly

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.env.Initializable
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv

interface MonopolyGame : OrderedGame {
    val inPrison: StateFlow<Boolean>

    fun addMoney(money: Int)

    fun spendMoney(money: Int)

    fun movePlayer(steps: Int)

    fun moveToPrison()

    fun leavePrison(rescued: Boolean)

    fun transferMoney(senderId: Long, receiverId: Long, money: Int)
}

class MonopolyGameEnv : OrderedGameEnv(Monopoly), MonopolyGame {
    private val currentPlayerState: Pair<Long, PlayerState>?
        get() = currentPlayer?.let { (id, player) -> id to player.state }

    override fun addPlayer(user: User?, name: String): Long {
        val state = monopolyPlayerState(
            score = 0,
            position = 1,
            inPrison = false
        )
        return addPlayer(user, name, state)
    }

    private val mInPrison = MutableStateFlow(false)
    override val inPrison: StateFlow<Boolean> = mInPrison.asStateFlow()

    private val inPrisonObserver = Initializable { scope ->
        currentPlayerId.combine(players) { id, p -> p[id] }
            .map { it?.state?.inPrison }
            .filterNotNull()
            .onEach { mInPrison.value = it }
            .launchIn(scope)
    }

    override val initializables: Array<Initializable> =
        super.initializables + inPrisonObserver

    override fun addMoney(money: Int) {
        currentPlayerId.value?.let {
            addMoney(it, money)
        }
    }

    private fun addMoney(playerId: Long, money: Int): PlayerState? {
        if (money <= 0) return null
        val state = players.value[playerId]?.state ?: return null
        return state.run {
            copy(score = score + money)
        }.also {
            changePlayerState(playerId, it)
        }
    }

    override fun spendMoney(money: Int) {
        currentPlayerId.value?.let {
            spendMoney(it, money)
        }
    }

    private fun spendMoney(playerId: Long, money: Int): PlayerState? {
        if (money <= 0) return null
        val state = players.value[playerId]?.state ?: return null
        if (state.score < money) return null
        return state.run {
            copy(score = score - money)
        }.also {
            changePlayerState(playerId, it)
        }
    }

    override fun movePlayer(steps: Int) {
        if (steps !in 2..12) return
        val (id, state) = currentPlayerState ?: return
        state.inPrison?.takeUnless { it } ?: return
        val position = state.position ?: return
        var newCycle = false
        val newPosState = (position + steps).let {
            if (it > MonopolyField.COUNT) {
                newCycle = true
                it % MonopolyField.COUNT
            } else {
                it
            }
        }.let {
            state.updatePosition(it)
        }
        changePlayerState(id, newPosState)
        if (newCycle) addMoney(id, Ahead.MONEY)
    }

    override fun moveToPrison() {
        val (id, state) = currentPlayerState ?: return
        state.inPrison?.takeUnless { it } ?: return
        val prisonPosState = state.updatePosition(Prison.POSITION)
        changePlayerState(id, prisonPosState)
        val inPrisonState = prisonPosState.updateInPrison(true)
        changePlayerState(id, inPrisonState)
    }

    override fun leavePrison(rescued: Boolean) {
        val (id, state) = currentPlayerState ?: return
        state.inPrison?.takeIf { it } ?: return
        val beforeFreeState =
            if (!rescued) spendMoney(id, Prison.FINE) ?: return
            else state
        changePlayerState(id, beforeFreeState.updateInPrison(false))
    }

    override fun transferMoney(senderId: Long, receiverId: Long, money: Int) {
        if (senderId == receiverId) return
        if (money <= 0) return
        val senderState = players.value[senderId]?.state ?: return
        if (senderState.score < money) return
        if (receiverId !in players.value) return
        spendMoney(senderId, money) ?: return
        addMoney(receiverId, money) ?: return
    }
}
