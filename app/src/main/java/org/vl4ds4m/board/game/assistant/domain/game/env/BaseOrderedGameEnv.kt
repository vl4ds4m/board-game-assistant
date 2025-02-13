package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.game.GameType
import org.vl4ds4m.board.game.assistant.domain.player.Player

class BaseOrderedGameEnv(type: GameType) : BaseGameEnv(type), OrderedGameEnv {
    private val mOrder: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val order: StateFlow<Int?> = mOrder.asStateFlow()

    override fun saveIn(session: GameSession) {
        super.saveIn(session)
        session.order = order.value
    }

    override fun loadFrom(session: GameSession) {
        super.loadFrom(session)
        mOrder.value = session.order
    }

    override fun nextOrder() {
        mOrder.update {
            it?.let { nextActive(players.value, it) }
        }
    }

    override fun changeOrderByPlayer(player: Player) {
        if (!player.active) {
            return
        }
        val index = players.value.indexOf(player)
        if (index == -1) {
            return
        }
        mOrder.update { index }
    }

    override fun changePlayerOrder(player: Player, order: Int) {
        if (order !in players.value.indices) {
            return
        }
        val index = players.value.indexOf(player)
        if (index == -1 || index == order) {
            return
        }
        val gameOrder = this.order.value
        if (gameOrder != null) {
            if (index == gameOrder) {
                mOrder.update { order }
            } else if (index < gameOrder && order >= gameOrder) {
                mOrder.update { gameOrder - 1 }
            } else if (index > gameOrder && order <= gameOrder) {
                mOrder.update { gameOrder + 1 }
            }
        }
        updatePlayers {
            removeAt(index)
            add(order, player)
        }
    }

    override fun addPlayer(playerName: String) {
        val hasActive = this.hasActive
        super.addPlayer(playerName)
        if (!hasActive) {
            mOrder.update { players.value.lastIndex }
        }
    }

    private val hasActive: Boolean
        get() = players.value.any { it.active }

    override fun removePlayer(player: Player) {
        val index = players.value.indexOf(player)
        if (index == -1) {
            return
        }
        val gameOrder = this.order.value
        if (gameOrder != null) {
            if (index < gameOrder) {
                mOrder.update { gameOrder - 1 }
            } else if (index == gameOrder) {
                val order = nextActive(players.value, gameOrder)
                    .takeUnless { it == gameOrder }
                    ?.let {
                        if (it < gameOrder) { it }
                        else { it - 1 }
                    }
                mOrder.update { order }
            }
        }
        updatePlayers {
            removeAt(index)
        }
    }

    override fun freezePlayer(player: Player) {
        if (!player.active) {
            return
        }
        val index = players.value.indexOf(player)
        if (index == -1) {
            return
        }
        val gameOrder = this.order.value
            ?: throw IllegalStateException("existed player is active => order must not be null")
        if (index == gameOrder) {
            val order = nextActive(players.value, gameOrder)
                .takeUnless { it == gameOrder }
            mOrder.update { order }
        }
        val frozenPlayer = player.copy(active = false)
        updatePlayers {
            removeAt(index)
            add(index, frozenPlayer)
        }
    }

    override fun unfreezePlayer(player: Player) {
        if (player.active) {
            return
        }
        val index = players.value.indexOf(player)
        if (index == -1) {
            return
        }
        if (this.order.value == null) {
            mOrder.update { index }
        }
        val activePlayer = player.copy(active = true)
        updatePlayers {
            removeAt(index)
            add(index, activePlayer)
        }
    }
}

internal fun nextActive(players: List<Player>, current: Int): Int? {
    if (current !in players.indices) {
        return null
    }
    var index = (current + 1) % players.size
    while (!players[index].active) {
        if (index == current) {
            return null
        }
        index = (index + 1) % players.size
    }
    return index
}
