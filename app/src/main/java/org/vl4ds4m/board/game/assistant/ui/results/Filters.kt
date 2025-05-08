package org.vl4ds4m.board.game.assistant.ui.results

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf

@Stable
class Filters<T>(finiteRules: Iterable<T>) {
    private val map = mutableStateMapOf<T, Boolean>().apply {
        finiteRules.forEach { put(it, true) }
    }

    val rules: Set<T> = map.keys.toSet()

    val none: Boolean get() = rules.all { map[it] == false }

    val all: Boolean get() = rules.all { map[it] == true }

    operator fun contains(filter: T): Boolean =
        map[filter] == true

    operator fun plusAssign(filter: T) {
        map.computeIfPresent(filter) { _, _ -> true }
    }

    operator fun minusAssign(filter: T) {
        map.computeIfPresent(filter) { _, _ -> false }
    }

    companion object {
        fun <T> empty() = Filters<T>(listOf())
    }
}
