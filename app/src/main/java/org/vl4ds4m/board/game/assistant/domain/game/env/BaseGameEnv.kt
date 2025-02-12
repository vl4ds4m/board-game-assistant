package org.vl4ds4m.board.game.assistant.domain.game.env

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.vl4ds4m.board.game.assistant.data.GameSession
import org.vl4ds4m.board.game.assistant.domain.player.Player
import java.util.concurrent.atomic.AtomicLong

open class BaseGameEnv : GameEnv {
    protected val mPlayers: MutableStateFlow<List<Player>> = MutableStateFlow(listOf())
    override val players: StateFlow<List<Player>> = mPlayers.asStateFlow()

    override val name: MutableStateFlow<String?> = MutableStateFlow(null)

    private var nextPlayerId: AtomicLong = AtomicLong(0)

    override fun saveIn(session: GameSession) {
        session.let {
            it.name = name.value
            it.players = players.value
            it.nextPlayerId = nextPlayerId.get()
        }
    }

    override fun loadFrom(session: GameSession) {
        session.let {
            name.value = it.name
            it.players?.let { list ->
                mPlayers.value = list
            }
            it.nextPlayerId?.let { nextId ->
                nextPlayerId.set(nextId)
            }
        }
    }

    override fun addPlayer(playerName: String) {
        val playerId = nextPlayerId.incrementAndGet()
        val newPlayer = Player(
            id = playerId,
            name = playerName,
            active = true,
            score = 0
        )
        updatePlayers {
            add(newPlayer)
        }
    }

    override fun renamePlayer(player: Player, name: String) {
        if (player.name == name) {
            return
        }
        val index = players.value.indexOf(player)
        if (index == -1) {
            return
        }
        val renamedPlayer = player.copy(name = name)
        updatePlayers {
            set(index, renamedPlayer)
        }
    }

    override fun changePlayerScore(player: Player, score: Int) {
        if (player.score == score || score < 0) {
            return
        }
        val index = players.value.indexOf(player)
        if (index == -1) {
            return
        }
        val updated = player.copy(score = score)
        updatePlayers {
            set(index, updated)
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
}
