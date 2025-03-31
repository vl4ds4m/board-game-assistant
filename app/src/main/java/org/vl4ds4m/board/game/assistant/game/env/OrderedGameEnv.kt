package org.vl4ds4m.board.game.assistant.game.env

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.data.GameState
import org.vl4ds4m.board.game.assistant.game.data.OrderedGameState
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.data.SimpleOrderedGameState
import org.vl4ds4m.board.game.assistant.updateAndGetStates
import org.vl4ds4m.board.game.assistant.updateList

open class OrderedGameEnv(type: GameType) : OrderedGame, GameEnv(type) {
    private val mNextPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    override val nextPlayerId = mCurrentPlayerId.asStateFlow()

    private val mOrderedPlayerIds: MutableStateFlow<List<Long>> = MutableStateFlow(listOf())
    override val orderedPlayerIds: StateFlow<List<Long>> = mOrderedPlayerIds.asStateFlow()

    override fun changeCurrentPlayerId() {
        mCurrentPlayerId.updateAndGetStates { currentId ->
            getNextActivePlayerId(
                players.value,
                orderedPlayerIds.value,
                currentId
            ).also {
                if (it == currentId) return
            }
        }.let {
            addActionForCurrentIdUpdate(it)
        }
    }

    override fun changePlayerOrder(id: Long, order: Int) {
        mOrderedPlayerIds.updateList {
            if (order !in indices) return
            val index = indexOf(id)
            if (index == -1 || index == order) return
            removeAt(index)
            add(order, id)
        }
    }

    override fun addPlayer(name: String, state: PlayerState): Long {
        val id = super.addPlayer(name, state)
        mOrderedPlayerIds.updateList { add(id) }
        return id
    }

    override fun removePlayer(id: Long) {
        val (players, _) = remove(id) ?: return
        val (ids, _) = mOrderedPlayerIds.updateList {
            val index = indexOf(id)
            if (index == -1) return
            removeAt(index)
        }
        updateCurrentIdOnEqual(id, players, ids)?.let {
            addActionForCurrentIdUpdate(it)
        }
    }

    private fun updateCurrentIdOnEqual(
        playerId: Long,
        players: Players,
        ids: List<Long>
    ): States<Long?>? {
        return mCurrentPlayerId.updateAndGetStates { currentId ->
            if (currentId != playerId) return null
            getNextActivePlayerId(players, ids, currentId)
                .takeUnless { it == currentId }
        }
    }

    override fun freezePlayer(id: Long) {
        val (players, _) = freeze(id) ?: return
        updateCurrentIdOnEqual(id, players, orderedPlayerIds.value)?.let {
            addActionForCurrentIdUpdate(it)
        }
    }

    private val nextPlayerIdObserver = object : Initializable {
        private var job: Job? = null

        override fun init(scope: CoroutineScope) {
            close()
            job = players.combine(orderedPlayerIds) { players, ids ->
                players to ids
            }.combine(currentPlayerId) { (players, ids), currentId ->
                getNextActivePlayerId(players, ids, currentId).let {
                    mNextPlayerId.value = it
                }
            }.launchIn(scope)
        }

        override fun close() {
            job?.cancel()
            job = null
        }
    }

    override val initializables: Array<Initializable> =
        super.initializables + nextPlayerIdObserver

    override fun restoreAdditionalState(state: GameState?) {
        super.restoreAdditionalState(state)
        state.let {
            it as? OrderedGameState
        }?.let {
            mOrderedPlayerIds.value = it.orderedPlayerIds
        }
    }

    override val additionalState: OrderedGameState
        get() = SimpleOrderedGameState(orderedPlayerIds.value)
}

private fun getNextActivePlayerId(
    players: Players,
    ids: List<Long>,
    currentId: Long?
): Long? {
    currentId ?: return null
    val startIndex = ids.indexOf(currentId)
    if (startIndex == -1) return null
    var index = startIndex
    while (true) {
        index = (index + 1) % ids.size
        val id = ids[index]
        if (players[id]?.active == true) break
        if (index == startIndex) return null
    }
    return ids[index]
}
