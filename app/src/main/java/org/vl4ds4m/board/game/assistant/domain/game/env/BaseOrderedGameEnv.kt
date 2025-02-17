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

    private val mCurrentPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    override val currentPlayerId: StateFlow<Long?> = mCurrentPlayerId.asStateFlow()

    override fun loadFrom(session: GameSession) {
        super.loadFrom(session)
        mCurrentPlayerId.value = session.state
            .let { it as? OrderedGameState }
            ?.currentPlayerId
    }

    override fun saveIn(session: GameSession) {
        super.saveIn(session)
        session.state = session.state.let {
            it as? OrderedGameState ?: OrderedGameState()
        }.also {
            it.currentPlayerId = this.currentPlayerId.value
        }
    }

    override fun selectNextPlayerId() {
        mCurrentPlayerId.update { currentId ->
            currentId?.let {
                nextActive(
                    ids = this.orderedPlayerIds.value,
                    players = this.players.value,
                    currentId = it
                )
            }
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

    override fun changeCurrentPlayerId(id: Long) {
        val player = players.value[id] ?: return
        if (!player.active) return
        mCurrentPlayerId.value = id
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
        mCurrentPlayerId.update { it ?: id }
        return id
    }

    override fun removePlayer(id: Long) {
        val gameEnv = this
        mOrderedPlayerIds.updateList {
            val ids = this
            val removed = remove(id)
            if (removed) {
                mCurrentPlayerId.update { currentId ->
                    if (id == currentId) {
                        nextActive(
                            ids = ids,
                            players = gameEnv.players.value,
                            currentId = currentId
                        )?.takeUnless { it == id }
                    } else {
                        currentId
                    }
                }
            }
        }
        super.removePlayer(id)
    }

    override fun freezePlayer(id: Long) {
        super.freezePlayer(id)
        mCurrentPlayerId.update { currentId ->
            if (id == currentId) {
                nextActive(
                    ids = this.orderedPlayerIds.value,
                    players = this.players.value,
                    currentId = currentId
                )?.takeUnless { it == id }
            } else {
                currentId
            }
        }
    }

    override fun unfreezePlayer(id: Long) {
        super.unfreezePlayer(id)
        mCurrentPlayerId.update {
            if (it == null && id in orderedPlayerIds.value) id
            else it
        }
    }
}
