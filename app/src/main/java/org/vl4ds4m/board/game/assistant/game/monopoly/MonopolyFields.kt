package org.vl4ds4m.board.game.assistant.game.monopoly

import org.vl4ds4m.board.game.assistant.game.monopoly.entity.MonopolyProperty
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.PowerStation
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.Terminal
import org.vl4ds4m.board.game.assistant.game.monopoly.entity.WaterStation

interface MonopolyField {
    companion object : MonopolyField {
        const val COUNT = 40
    }
}

class MonopolyPenalty(val value: Int) : MonopolyField

data object Ahead : MonopolyField {
    const val MONEY = 2_000_000
}

data object Prison : MonopolyField {
    const val POSITION = 11
    const val FINE = 500_000
}

data object GoToPrison : MonopolyField

val monopolyFields: Map<Int, MonopolyField> = mapOf(
    1  to Ahead,
    2  to MonopolyProperty.A[0],
    3  to MonopolyField,
    4  to MonopolyProperty.A[1],
    5  to MonopolyPenalty(2_000_000),
    6  to Terminal.A,
    7  to MonopolyProperty.B[0],
    8  to MonopolyField,
    9  to MonopolyProperty.B[1],
    10 to MonopolyProperty.B[2],

    11 to Prison,
    12 to MonopolyProperty.C[0],
    13 to PowerStation,
    14 to MonopolyProperty.C[1],
    15 to MonopolyProperty.C[2],
    16 to Terminal.B,
    17 to MonopolyProperty.D[0],
    18 to MonopolyField,
    19 to MonopolyProperty.D[1],
    20 to MonopolyProperty.D[2],

    21 to MonopolyField, // Camp
    22 to MonopolyProperty.E[0],
    23 to MonopolyField,
    24 to MonopolyProperty.E[1],
    25 to MonopolyProperty.E[2],
    26 to Terminal.C,
    27 to MonopolyProperty.F[0],
    28 to MonopolyProperty.F[1],
    29 to WaterStation,
    30 to MonopolyProperty.F[2],

    31 to GoToPrison,
    32 to MonopolyProperty.G[0],
    33 to MonopolyProperty.G[1],
    34 to MonopolyField,
    35 to MonopolyProperty.G[2],
    36 to Terminal.D,
    37 to MonopolyField,
    38 to MonopolyProperty.H[0],
    39 to MonopolyPenalty(1_000_000),
    40 to MonopolyProperty.H[1],
)
    .also { if (it.size != 40) throw AssertionError() }
