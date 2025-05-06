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
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.env.Initializable
import org.vl4ds4m.board.game.assistant.game.env.OrderedGameEnv
import org.vl4ds4m.board.game.assistant.game.log.GameAction
import org.vl4ds4m.board.game.assistant.updateMap

interface MonopolyGame : OrderedGame {
    val inPrison: StateFlow<Boolean>

    fun addMoney(money: Int)

    fun spendMoney(money: Int)

    fun movePlayer(steps: Int)

    fun moveToPrison()

    fun leavePrison(rescued: Boolean)

    fun transferMoney(senderId: PID, receiverId: PID, money: Int)
}

class MonopolyGameEnv : OrderedGameEnv(Monopoly), MonopolyGame {
    private val currentPlayerState: Pair<PID, PlayerState>?
        get() = currentPlayer?.let { (id, player) -> id to player.state }

    override fun addPlayer(user: User?, name: String): PID {
        val state = monopolyStartPlayerState()
        return addPlayer(user, name, state)
    }

    private val mInPrison = MutableStateFlow(false)
    override val inPrison: StateFlow<Boolean> = mInPrison.asStateFlow()

    private val inPrisonObserver = Initializable { scope ->
        currentPid.combine(players) { id, p -> p[id] }
            .map { it?.state?.inPrison }
            .filterNotNull()
            .onEach { mInPrison.value = it }
            .launchIn(scope)
    }

    override val initializables: Array<Initializable> =
        super.initializables + inPrisonObserver

    override fun addMoney(money: Int) {
        currentPlayerState?.let { (id, state) ->
            addMoney(state, money)?.let { newState ->
                changePlayerState(id, newState)
            }
        }
    }

    private fun addMoney(state: PlayerState, money: Int): PlayerState? {
        if (money <= 0) return null
        return state.run {
            copy(score = score + money)
        }

    }

    override fun spendMoney(money: Int) {
        currentPlayerState?.let { (id, state) ->
            spendMoney(state, money)?.let { newState ->
                changePlayerState(id, newState)
            }
        }
    }

    private fun spendMoney(state: PlayerState, money: Int): PlayerState? {
        if (money <= 0) return null
        if (state.score < money) return null
        return state.run {
            copy(score = score - money)
        }
    }

    override fun movePlayer(steps: Int) {
        if (steps !in 2..12) return
        val (id, state) = currentPlayerState ?: return
        state.inPrison?.takeUnless { it } ?: return
        val position = state.position ?: return
        var newCycle = false
        var newState = (position + steps).let {
            if (it > MonopolyField.COUNT) {
                newCycle = true
                it % MonopolyField.COUNT
            } else {
                it
            }
        }.let {
            state.updatePosition(it)
        }
        if (newCycle) {
            newState = addMoney(newState, Ahead.MONEY) ?: return
        }
        changePlayerState(id, newState)
    }

    override fun moveToPrison() {
        val (id, state) = currentPlayerState ?: return
        state.inPrison?.takeUnless { it } ?: return
        val newState = state
            .updatePosition(Prison.POSITION)
            .updateInPrison(true)
        changePlayerState(id, newState)
    }

    override fun leavePrison(rescued: Boolean) {
        val (id, state) = currentPlayerState ?: return
        state.inPrison?.takeIf { it } ?: return
        var newState = if (rescued) state
            else spendMoney(state, Prison.FINE) ?: return
        newState = newState.updateInPrison(false)
        changePlayerState(id, newState)
    }

    override fun transferMoney(senderId: PID, receiverId: PID, money: Int) {
        if (senderId == receiverId) return
        if (money <= 0) return
        val sender = players.value[senderId] ?: return
        if (sender.state.score < money) return
        val receiver = players.value[receiverId] ?: return
        val newSenderState = spendMoney(sender.state, money) ?: return
        val newReceiverState = addMoney(receiver.state, money) ?: return
        mPlayers.updateMap {
            put(senderId, sender.copy(state = newSenderState))
            put(receiverId, receiver.copy(state = newReceiverState))
        }
        history += monopolyPlayersCashChangedAction(
            senderId = senderId,
            receiverId = receiverId,
            amount = money
        )
    }

    override fun revert(action: GameAction) {
        when {
            action.changesMonopolyPlayersCash -> {
                updatePlayersCash(action, true)
            }
            else -> super.revert(action)
        }
    }

    override fun repeat(action: GameAction) {
        when {
            action.changesMonopolyPlayersCash -> {
                updatePlayersCash(action, false)
            }
            else -> super.repeat(action)
        }
    }

    private fun updatePlayersCash(action: GameAction, revert: Boolean) {
        val senderId = action.senderId ?: return
        val receiverId = action.receiverId ?: return
        val amount = action.monopolyAmount
            ?.let { if (revert) -it else it }
            ?: return
        mPlayers.updateMap {
            val sender = get(senderId) ?: return
            val receiver = get(receiverId) ?: return
            val senderState = sender.state.run {
                copy(score = score - amount)
            }
            val receiverState = receiver.state.run {
                copy(score = score + amount)
            }
            put(senderId, sender.copy(state = senderState))
            put(receiverId, receiver.copy(state = receiverState))
        }
    }
}
