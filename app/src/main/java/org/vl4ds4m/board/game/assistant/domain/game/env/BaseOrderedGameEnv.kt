package org.vl4ds4m.board.game.assistant.domain.game.env

import org.vl4ds4m.board.game.assistant.domain.player.Player

abstract class BaseOrderedGameEnv : BaseGameEnv(), OrderedGameEnv {
    protected val orderedPlayers = mutableListOf<Player>()

    override var order: Int = 0
        get() {
            return if (playersStates.isEmpty()) 0
            else field % playersStates.size
        }
        set(value) {
            field = if (playersStates.isEmpty()) 0
            else value % playersStates.size
        }

    override fun changePlayerOrder(player: Player, newOrder: Int) {
        orderedPlayers.remove(player)
        orderedPlayers.add(newOrder, player)
    }

    override fun addPlayer(newPlayer: Player) {
        super.addPlayer(newPlayer)
        orderedPlayers.add(newPlayer)
    }

    override fun removePlayer(player: Player) {
        super.removePlayer(player)
        orderedPlayers.remove(player)
    }
}
