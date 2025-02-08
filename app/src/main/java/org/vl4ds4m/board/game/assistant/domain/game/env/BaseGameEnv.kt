package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.domain.player.Player
import java.util.concurrent.atomic.AtomicLong

open class BaseGameEnv : GameEnv {
    protected val mPlayers: MutableStateFlow<List<Player>> = MutableStateFlow(listOf())
    override val players: StateFlow<List<Player>> = mPlayers.asStateFlow()

    override var name: MutableStateFlow<String?> = MutableStateFlow(null)

    private var nextPlayerId: AtomicLong = AtomicLong(0)

    override fun addPlayer(playerName: String) {
        val playerId = nextPlayerId.incrementAndGet()
        val newPlayer = Player(
            id = playerId,
            name = playerName,
            active = true
        )
        updatePlayers {
            add(newPlayer)
        }
    }

    override fun changePlayerName(player: Player, name: String) {
        if (player.name == name) {
            return
        }
        val index = players.value.indexOf(player)
        if (index == -1) {
            return
        }
        val renamedPlayer = player.copy(name = name)
        updatePlayers {
            removeAt(index)
            add(index, renamedPlayer)
        }
    }

    override fun removePlayer(player: Player) {
        updatePlayers {
            remove(player)
        }
    }

    override fun freezePlayer(player: Player) {
        updatePlayerActivity(player, false)
    }

    override fun unfreezePlayer(player: Player) {
        updatePlayerActivity(player, true)
    }

    private fun updatePlayerActivity(player: Player, active: Boolean) {
        if (player.active == active) {
            return
        }
        val updatedPlayer = player.copy(active = active)
        updatePlayers {
            val exists = remove(player)
            if (exists) {
                add(updatedPlayer)
            }
        }
    }

    protected inline fun updatePlayers(action: MutableList<Player>.() -> Unit) {
        mPlayers.update {
            buildList {
                addAll(it)
                action()
            }
        }
    }

    /*fun save() {
        val session = Session(
            name = name!!,
            type = GameType.ORDERED,
            players = getPlayers(),
            startTime = Instant.now(),
            stopTime = Instant.now(),
            completed = false
        )
        Store.sessions.add(session)
    }

    fun restore(sessionIndex: Int) {
        val session = Store.sessions[sessionIndex]
        this.name = session.name
        this.players.clear()
        this.players.addAll(session.players)
    }*/
}
