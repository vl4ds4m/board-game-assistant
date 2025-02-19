package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.game.state.OrderedGameState
import org.vl4ds4m.board.game.assistant.domain.Player
import org.vl4ds4m.board.game.assistant.util.updateList

class BaseOrderedGameEnv(type: GameType) : BaseGameEnv(type), OrderedGameEnv {
    private val mOrderedPlayerIds: MutableStateFlow<List<Long>> = MutableStateFlow(listOf())
    override val orderedPlayerIds: StateFlow<List<Long>> = mOrderedPlayerIds.asStateFlow()

    override fun selectNextPlayerId() {
        mCurrentPlayerId.update { currentId ->
            currentId?.let {
                nextActive(orderedPlayerIds.value, players.value, it)
            }
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

    override fun addPlayer(name: String): Long {
        val id = super.addPlayer(name)
        mOrderedPlayerIds.updateList { add(id) }
        return id
    }

    override fun removePlayer(id: Long) {
        mOrderedPlayerIds.updateList {
            if (remove(id)) {
                updateOnEqual(this, players.value, id)
            }
        }
        super.removePlayer(id)
    }

    private fun updateOnEqual(ids: List<Long>, players: Map<Long, Player>, playerId: Long) {
        mCurrentPlayerId.update { currentId ->
            if (playerId == currentId) {
                nextActive(ids, players, currentId)
                    .takeUnless { it == currentId }
            } else {
                currentId
            }
        }
    }

    override fun freezePlayer(id: Long) {
        updateOnEqual(orderedPlayerIds.value, players.value, id)
        super.freezePlayer(id)
    }

    override fun loadFrom(session: GameSession) {
        super.loadFrom(session)
        session.state.let {
            it as? OrderedGameState
        }?.let {
            this.mOrderedPlayerIds.value = it.orderedPlayerIds
        }
    }

    override fun saveIn(session: GameSession) {
        session.state = session.state.let {
            it as? OrderedGameState ?: OrderedGameState()
        }.also {
            it.orderedPlayerIds = this.orderedPlayerIds.value
        }
        super.saveIn(session)
    }
}

private fun nextActive(ids: List<Long>, players: Map<Long, Player>, currentId: Long): Long? {
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
