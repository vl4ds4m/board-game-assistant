package org.vl4ds4m.board.game.assistant.game

import kotlinx.serialization.Serializable
import org.vl4ds4m.board.game.assistant.data.User
import org.vl4ds4m.board.game.assistant.game.data.PlayerState

@Serializable
data class Player(
    val user: User?,
    val name: String,
    val active: Boolean,
    val state: PlayerState
) : Comparable<Player> {
    override fun compareTo(other: Player): Int = when {
        this.active != other.active -> {
            if (this.active) -1
            else 1
        }
        else -> other.state.score.compareTo(this.state.score)
    }
}
