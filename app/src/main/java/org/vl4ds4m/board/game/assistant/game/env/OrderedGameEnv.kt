package org.vl4ds4m.board.game.assistant.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.vl4ds4m.board.game.assistant.States
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.updateAndGetStates
import org.vl4ds4m.board.game.assistant.updateList

open class OrderedGameEnv(type: GameType) : OrderedGame, GameEnv(type) {
    private val mNextPlayerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    final override val nextPlayerId = mCurrentPlayerId.asStateFlow()

    private val nextPlayerIdUpdater = Initializable { scope ->
        players.combine(orderedPlayerIds) { ps, ids ->
            ps to ids
        }.combine(currentPlayerId) { (ps, ids), id ->
            val nextId = getNextActivePlayerId(ps, ids, id)
            mNextPlayerId.value = nextId
        }.launchIn(scope)
    }

    private val orderedPlayerIds = MutableStateFlow<List<Long>>(listOf())

    final override fun changeCurrentPlayerId() {
        mCurrentPlayerId.updateAndGetStates { currentId ->
            getNextActivePlayerId(
                players.value,
                orderedPlayerIds.value,
                currentId
            ).also {
                if (it == currentId) return
            }
        }.let {
            addCurrentPlayerChangedAction(it)
        }
    }

    final override fun changePlayerOrder(id: Long, order: Int) {
        orderedPlayerIds.updateList {
            if (order !in indices) return
            val index = indexOf(id)
            if (index == -1 || index == order) return
            removeAt(index)
            add(order, id)
        }
    }

    final override val orderedPlayersUpdater = Initializable { scope ->
        players.combine(orderedPlayerIds) { ps, ids ->
            mOrderedPlayers.value = ids.mapNotNull { id ->
                ps[id]?.let { p -> id to p }
            }
        }.launchIn(scope)
    }

    final override fun addPlayer(user: User?, name: String, state: PlayerState): Long {
        val id = super.addPlayer(user, name, state)
        orderedPlayerIds.updateList { add(id) }
        return id
    }

    final override fun removePlayer(id: Long) {
        val ids = orderedPlayerIds.value
        val (players, _) = remove(id) ?: return
        val states = updateCurrentIdOnEqual(id, players, ids) ?: return
        addCurrentPlayerChangedAction(states)
    }

    private fun updateCurrentIdOnEqual(
        playerId: Long,
        players: Players,
        ids: List<Long>
    ): States<Long?>? = mCurrentPlayerId.updateAndGetStates { currentId ->
        if (currentId != playerId) return null
        getNextActivePlayerId(players, ids, currentId)
            .takeIf { it != currentId }
    }

    final override fun freezePlayer(id: Long) {
        val (players, _) = freeze(id) ?: return
        val ids = updateCurrentIdOnEqual(id, players, orderedPlayerIds.value) ?: return
        addCurrentPlayerChangedAction(ids)
    }

    override val initializables: Array<Initializable> =
        super.initializables + nextPlayerIdUpdater

    final override var mPlayersIds: List<Long>
        get() = orderedPlayerIds.value
        set(value) {
            orderedPlayerIds.value = value
        }
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
