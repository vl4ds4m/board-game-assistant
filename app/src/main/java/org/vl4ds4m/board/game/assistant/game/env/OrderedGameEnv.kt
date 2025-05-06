package org.vl4ds4m.board.game.assistant.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.vl4ds4m.board.game.assistant.States
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.game.OrderedGame
import org.vl4ds4m.board.game.assistant.game.PID
import org.vl4ds4m.board.game.assistant.game.Players
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.updateAndGetStates
import org.vl4ds4m.board.game.assistant.updateList

open class OrderedGameEnv(type: GameType) : OrderedGame, GameEnv(type) {
    private val mNextPid: MutableStateFlow<PID?> = MutableStateFlow(null)
    final override val nextPid = mCurrentPid.asStateFlow()

    private val nextPidUpdater = Initializable { scope ->
        players.combine(orderedPIDs) { ps, ids ->
            ps to ids
        }.combine(currentPid) { (ps, ids), id ->
            val nextId = getNextActivePlayerId(ps, ids, id)
            mNextPid.value = nextId
        }.launchIn(scope)
    }

    private val orderedPIDs = MutableStateFlow<List<PID>>(listOf())

    final override fun changeCurrentPid() {
        mCurrentPid.updateAndGetStates { currentId ->
            getNextActivePlayerId(
                players.value,
                orderedPIDs.value,
                currentId
            ).also {
                if (it == currentId) return
            }
        }.let {
            addCurrentPlayerChangedAction(it)
        }
    }

    final override fun changePlayerOrder(id: PID, order: Int) {
        orderedPIDs.updateList {
            if (order !in indices) return
            val index = indexOf(id)
            if (index == -1 || index == order) return
            removeAt(index)
            add(order, id)
        }
    }

    final override val orderedPlayersUpdater = Initializable { scope ->
        players.combine(orderedPIDs) { ps, ids ->
            mOrderedPlayers.value = ids.mapNotNull { id ->
                ps[id]?.let { p -> id to p }
            }
        }.launchIn(scope)
    }

    final override fun addPlayer(user: User?, name: String, state: PlayerState): PID {
        val id = super.addPlayer(user, name, state)
        orderedPIDs.updateList { add(id) }
        return id
    }

    final override fun removePlayer(id: PID) {
        val ids = orderedPIDs.value
        val (players, _) = remove(id) ?: return
        val states = updateCurrentIdOnEqual(id, players, ids) ?: return
        addCurrentPlayerChangedAction(states)
    }

    private fun updateCurrentIdOnEqual(
        playerId: PID,
        players: Players,
        ids: List<PID>
    ): States<PID?>? = mCurrentPid.updateAndGetStates { currentId ->
        if (currentId != playerId) return null
        getNextActivePlayerId(players, ids, currentId)
            .takeIf { it != currentId }
    }

    final override fun freezePlayer(id: PID) {
        val (players, _) = freeze(id) ?: return
        val ids = updateCurrentIdOnEqual(id, players, orderedPIDs.value) ?: return
        addCurrentPlayerChangedAction(ids)
    }

    override val initializables: Array<Initializable> =
        super.initializables + nextPidUpdater

    final override var mPIDs: List<PID>
        get() = orderedPIDs.value
        set(value) {
            orderedPIDs.value = value
        }
}

private fun getNextActivePlayerId(
    players: Players,
    ids: List<PID>,
    currentId: PID?
): PID? {
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
