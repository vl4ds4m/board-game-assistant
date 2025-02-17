package org.vl4ds4m.board.game.assistant.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

val Enum<*>.title: String get() = this.name
    .replaceFirstChar { it.uppercaseChar() }

inline fun <reified T> MutableStateFlow<List<T>>.updateList(
    action: MutableList<T>.() -> Unit
) {
    this.update { oldList ->
        val newList = buildList {
            addAll(oldList)
            action()
        }
        return@update newList
    }
}

inline fun <reified K, V> MutableStateFlow<Map<K, V>>.updateMap(
    action: MutableMap<K, V>.() -> Unit
) {
    this.update { oldMap ->
        val newMap = buildMap {
            putAll(oldMap)
            action()
        }
        return@update newMap
    }
}
