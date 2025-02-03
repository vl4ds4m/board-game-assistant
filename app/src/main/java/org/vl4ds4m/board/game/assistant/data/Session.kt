package org.vl4ds4m.board.game.assistant.data

import org.vl4ds4m.board.game.assistant.domain.game.GameType

data class Session(
    val id: Long,
    val name: String,
    val type: GameType,
    val players: List<String>
) {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return this === other ||
                other is Session && this.id == other.id
    }
}
