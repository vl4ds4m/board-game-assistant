package org.vl4ds4m.board.game.assistant.domain.game.monopoly

import org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity.MonopolyProperty
import org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity.PowerStation
import org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity.Terminal
import org.vl4ds4m.board.game.assistant.domain.game.monopoly.entity.WaterStation

interface MonopolyField

object MonopolyCard : MonopolyField
object MonopolyPenalty : MonopolyField

private val String.field: MonopolyField
    get() = object : MonopolyField {}

val MonopolyFields: Map<Int, MonopolyField> = mapOf(
    1  to "Ahead".field,
    2  to MonopolyProperty.A[0],
    3  to MonopolyCard,
    4  to MonopolyProperty.A[1],
    5  to MonopolyPenalty,
    6  to Terminal.A,
    7  to MonopolyProperty.B[0],
    8  to MonopolyCard,
    9  to MonopolyProperty.B[1],
    10 to MonopolyProperty.B[2],

    11 to "Prison".field,
    12 to MonopolyProperty.C[0],
    13 to PowerStation,
    14 to MonopolyProperty.C[1],
    15 to MonopolyProperty.C[2],
    16 to Terminal.B,
    17 to MonopolyProperty.D[0],
    18 to MonopolyCard,
    19 to MonopolyProperty.D[1],
    20 to MonopolyProperty.D[2],

    21 to "Camp".field,
    22 to MonopolyProperty.E[0],
    23 to MonopolyCard,
    24 to MonopolyProperty.E[1],
    25 to MonopolyProperty.E[2],
    26 to Terminal.C,
    27 to MonopolyProperty.F[0],
    28 to MonopolyProperty.F[1],
    29 to WaterStation,
    30 to MonopolyProperty.F[2],

    31 to "ToPrison".field,
    32 to MonopolyProperty.G[0],
    33 to MonopolyProperty.G[1],
    34 to MonopolyCard,
    35 to MonopolyProperty.G[2],
    36 to Terminal.D,
    37 to MonopolyCard,
    38 to MonopolyProperty.H[0],
    39 to MonopolyPenalty,
    40 to MonopolyProperty.H[1],
)
    .also { if (it.size != 40) throw AssertionError() }
