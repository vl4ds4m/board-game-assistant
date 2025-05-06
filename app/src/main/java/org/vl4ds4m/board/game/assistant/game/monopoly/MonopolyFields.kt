package org.vl4ds4m.board.game.assistant.game.monopoly

import androidx.annotation.StringRes
import org.vl4ds4m.board.game.assistant.R

interface MonopolyField {
    @get:StringRes
    val resId: Int

    companion object {
        const val COUNT = 40
    }
}

class MonopolyPenalty(val value: Int) : MonopolyField {
    override val resId = R.string.monopoly_field_penalty
}

object Ahead : MonopolyField {
    override val resId = R.string.monopoly_field_ahead

    const val MONEY = 2_000
}

object Prison : MonopolyField {
    override val resId = R.string.monopoly_field_prison

    const val POSITION = 11
    const val FINE = 500
}

object Parking : MonopolyField {
    override val resId = R.string.monopoly_field_parking
}

object GoToPrison : MonopolyField {
    override val resId = R.string.monopoly_field_goto_prison
}

object CommunityChest : MonopolyField {
    override val resId = R.string.monopoly_field_community_chest
}

object Chance : MonopolyField {
    override val resId = R.string.monopoly_field_chance
}

object Station {
    val Power = object : MonopolyField {
        override val resId = R.string.monopoly_field_power_station
    }

    val Water = object : MonopolyField {
        override val resId = R.string.monopoly_field_water_station
    }
}

object Terminal {
    val A = object : MonopolyField {
        override val resId = R.string.monopoly_field_terminal_a
    }

    val B = object : MonopolyField {
        override val resId = R.string.monopoly_field_terminal_b
    }

    val C = object : MonopolyField {
        override val resId = R.string.monopoly_field_terminal_c
    }

    val D = object : MonopolyField {
        override val resId = R.string.monopoly_field_terminal_d
    }
}

val monopolyFields: Map<Int, MonopolyField> = mapOf(
    1  to Ahead,
    2  to Property.A[0],
    3  to CommunityChest,
    4  to Property.A[1],
    5  to MonopolyPenalty(2_000),
    6  to Terminal.A,
    7  to Property.B[0],
    8  to Chance,
    9  to Property.B[1],
    10 to Property.B[2],

    11 to Prison,
    12 to Property.C[0],
    13 to Station.Power,
    14 to Property.C[1],
    15 to Property.C[2],
    16 to Terminal.B,
    17 to Property.D[0],
    18 to CommunityChest,
    19 to Property.D[1],
    20 to Property.D[2],

    21 to Parking,
    22 to Property.E[0],
    23 to Chance,
    24 to Property.E[1],
    25 to Property.E[2],
    26 to Terminal.C,
    27 to Property.F[0],
    28 to Property.F[1],
    29 to Station.Water,
    30 to Property.F[2],

    31 to GoToPrison,
    32 to Property.G[0],
    33 to Property.G[1],
    34 to CommunityChest,
    35 to Property.G[2],
    36 to Terminal.D,
    37 to Chance,
    38 to Property.H[0],
    39 to MonopolyPenalty(1_000),
    40 to Property.H[1],
).also {
    if (it.size != 40) throw AssertionError()
}
