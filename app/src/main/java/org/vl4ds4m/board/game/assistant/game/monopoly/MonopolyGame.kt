package org.vl4ds4m.board.game.assistant.game.monopoly

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.data.MonopolyPlayerState
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
    private val currentPlayerState: Pair<Long, MonopolyPlayerState>?
        get() = currentPlayer?.let { (id, p) ->
            p.monopolyState?.let { state -> id to state }
        }

    override fun addPlayer(netDevId: String?, name: String) {
        addPlayer(netDevId, name, MonopolyPlayerState())
    }

    override fun producePlayerState(
        source: PlayerState, provider: PlayerState
    ): MonopolyPlayerState {
        val init = source as MonopolyPlayerState
        val prov = provider as MonopolyPlayerState
        return when {
            init.position != prov.position -> init.copy(position = prov.position)
            init.inPrison != prov.inPrison -> init.copy(inPrison = prov.inPrison)
            else -> init.copy(score = prov.score)
        }
    }

    private val mInPrison = MutableStateFlow(false)
    override val inPrison: StateFlow<Boolean> = mInPrison.asStateFlow()

    private val inPrisonObserver = Initializable { scope ->
        currentPlayerId.combine(players) { id, p -> p[id] }
            .filterNotNull()
            .map { it.state as MonopolyPlayerState }
            .onEach { mInPrison.value = it.inPrison }
            .launchIn(scope)
    }

    override val initializables: Array<Initializable> =
        super.initializables + inPrisonObserver

    override fun addMoney(money: Int) {
        currentPlayerId.value?.let {
            addMoney(it, money)
        }
    }

    private fun addMoney(playerId: Long, money: Int): MonopolyPlayerState? {
        if (money <= 0) return null
        val state = players.value[playerId]?.monopolyState ?: return null
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

    private fun spendMoney(playerId: Long, money: Int): MonopolyPlayerState? {
        if (money <= 0) return null
        val state = players.value[playerId]?.monopolyState ?: return null
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
        if (state.inPrison) return
        var newCycle = false
        val newPosState = (state.position + steps).let {
            if (it > MonopolyField.COUNT) {
                newCycle = true
                it % MonopolyField.COUNT
            } else {
                it
            }
        }.let {
            state.copy(position = it)
        }
        changePlayerState(id, newPosState)
        if (newCycle) addMoney(id, Ahead.MONEY)
    }

    override fun moveToPrison() {
        val (id, state) = currentPlayerState ?: return
        if (state.inPrison) return
        val prisonPosState = state.copy(position = Prison.POSITION)
        changePlayerState(id, prisonPosState)
        changePlayerState(id, prisonPosState.copy(inPrison = true))
    }

    override fun leavePrison(rescued: Boolean) {
        val (id, state) = currentPlayerState ?: return
        if (!state.inPrison) return
        val beforeFreeState =
            if (!rescued) spendMoney(id, Prison.FINE) ?: return
            else state
        changePlayerState(id, beforeFreeState.copy(inPrison = false))
    }

    override fun transferMoney(senderId: Long, receiverId: Long, money: Int) {
        if (money <= 0) return
        val senderState = players.value[senderId]?.monopolyState ?: return
        if (senderState.score < money) return
        if (receiverId !in players.value) return
        spendMoney(senderId, money) ?: return
        addMoney(receiverId, money) ?: return
    }
}

private val Player.monopolyState: MonopolyPlayerState?
    get() = state as? MonopolyPlayerState
