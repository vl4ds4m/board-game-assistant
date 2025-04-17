package org.vl4ds4m.board.game.assistant.game.monopoly

import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.game.data.copy

fun monopolyPlayerState(score: Int, position: Int, inPrison: Boolean) =
    PlayerState(
        score = score,
        data = mapOf(
            POSITION_KEY to position.toString(),
            IN_PRISON_KEY to inPrison.toString()
        )
    )

val PlayerState.position: Int?
    get() = data[POSITION_KEY]?.toIntOrNull()

val PlayerState.inPrison: Boolean?
    get() = data[IN_PRISON_KEY]?.toBooleanStrictOrNull()

fun PlayerState.updatePosition(position: Int): PlayerState = copy(
    data = data.copy(POSITION_KEY to position.toString())
)

fun PlayerState.updateInPrison(inPrison: Boolean): PlayerState = copy(
    data = data.copy(IN_PRISON_KEY to inPrison.toString())
)

private const val POSITION_KEY = "position"
private const val IN_PRISON_KEY = "in_prison"
