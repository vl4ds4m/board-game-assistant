package org.vl4ds4m.board.game.assistant.game.monopoly.entity

sealed interface MonopolyProperty : MonopolyEntity {
    companion object {
        private data object A1 : MonopolyProperty
        private data object A2 : MonopolyProperty
        val A: List<MonopolyProperty> = listOf(A1, A2)

        private data object B1 : MonopolyProperty
        private data object B2 : MonopolyProperty
        private data object B3 : MonopolyProperty
        val B: List<MonopolyProperty> = listOf(B1, B2, B3)

        private data object C1 : MonopolyProperty
        private data object C2 : MonopolyProperty
        private data object C3 : MonopolyProperty
        val C: List<MonopolyProperty> = listOf(C1, C2, C3)

        private data object D1 : MonopolyProperty
        private data object D2 : MonopolyProperty
        private data object D3 : MonopolyProperty
        val D: List<MonopolyProperty> = listOf(D1, D2, D3)

        private data object E1 : MonopolyProperty
        private data object E2 : MonopolyProperty
        private data object E3 : MonopolyProperty
        val E: List<MonopolyProperty> = listOf(E1, E2, E3)

        private data object F1 : MonopolyProperty
        private data object F2 : MonopolyProperty
        private data object F3 : MonopolyProperty
        val F: List<MonopolyProperty> = listOf(F1, F2, F3)

        private data object G1 : MonopolyProperty
        private data object G2 : MonopolyProperty
        private data object G3 : MonopolyProperty
        val G: List<MonopolyProperty> = listOf(G1, G2, G3)

        private data object H1 : MonopolyProperty
        private data object H2 : MonopolyProperty
        val H: List<MonopolyProperty> = listOf(H1, H2)
    }
}
