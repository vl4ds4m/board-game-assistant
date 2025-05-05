package org.vl4ds4m.board.game.assistant.game

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

@Serializable
data class Player(
    val user: User?,
    val name: String,
    val presence: Presence,
    val state: PlayerState
) : Comparable<Player> {
    constructor(user: User?, name: String, state: PlayerState) :
        this(user, name, Presence.ACTIVE, state)

    val active:  Boolean get() = presence == Presence.ACTIVE
    val frozen:  Boolean get() = presence == Presence.FROZEN
    val removed: Boolean get() = presence == Presence.REMOVED

    override fun compareTo(other: Player): Int = when {
        this.presence != other.presence -> {
            if (this.presence == Presence.ACTIVE) -1
            else 1
        }
        else -> other.state.score.compareTo(this.state.score)
    }

    enum class Presence {
        ACTIVE, FROZEN, REMOVED
    }
}
