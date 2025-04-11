package org.vl4ds4m.board.game.assistant.game.monopoly

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.vl4ds4m.board.game.assistant.game.Monopoly
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.data.MonopolyPlayerState
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.env.Initializable
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv

class MonopolyGame : OrderedGameEnv(Monopoly) {
    private val currentPlayerState: Pair<Long, MonopolyPlayerState>?
        get() = currentPlayer?.let { (id, p) ->
            p.monopolyState?.let { state -> id to state }
        }

    private val mCurrentField = MutableStateFlow<MonopolyField?>(null)
    val currentField: StateFlow<MonopolyField?> = mCurrentField.asStateFlow()

    val currentFieldObserver: Initializable = Initializable { scope ->
        players.combine(currentPlayerId) { players, currentId ->
            mCurrentField.value = currentId?.let { id ->
                players[id]?.monopolyState?.position?.let {
                    monopolyFields[it]
                }
            }
        }.launchIn(scope)
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

    fun addMoney(money: Int) {
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

    fun spendMoney(money: Int) {
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

    fun movePlayer(steps: Int) {
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

    fun moveToPrison() {
        if (currentField.value != GoToPrison) return
        val (id, state) = currentPlayerState ?: return
        if (state.inPrison) return
        val prisonPosState = state.copy(position = Prison.POSITION)
        changePlayerState(id, prisonPosState)
        changePlayerState(id, prisonPosState.copy(inPrison = true))
    }

    fun leavePrison(rescued: Boolean) {
        val (id, state) = currentPlayerState ?: return
        if (!state.inPrison) return
        val beforeFreeState =
            if (!rescued) spendMoney(id, Prison.FINE) ?: return
            else state
        changePlayerState(id, beforeFreeState.copy(inPrison = false))
    }

    fun transferMoney(senderId: Long, receiverId: Long, money: Int) {
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
