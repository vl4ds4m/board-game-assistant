package org.vl4ds4m.board.game.assistant.domain.player.state

@JvmInline
value class Score(override val value: Int) : PlayerState<Int> {
    override fun compareTo(other: PlayerState<Int>): Int {
        return this.value.compareTo(other.value)
    }
}
