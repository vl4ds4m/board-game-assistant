package org.vl4ds4m.board.game.assistant

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.Closeable
import java.io.IOException

val Enum<*>.title: String get() = this.name
    .replaceFirstChar { it.uppercaseChar() }

val String.short: String get() =
    if (length <= 20) this
    else substring(0, 20) + "..."

inline fun <K, V> MutableStateFlow<Map<K, V>>.updateMap(
    action: MutableMap<K, V>.() -> Unit
): Pair<Map<K, V>, Map<K, V>> {
    return updateAndGetStates { it.toMutableMap().apply(action) }
}

inline fun <E> MutableStateFlow<List<E>>.updateList(
    action: MutableList<E>.() -> Unit
): Pair<List<E>, List<E>> {
    return updateAndGetStates { it.toMutableList().apply(action) }
}

inline fun <T> MutableStateFlow<T>.updateAndGetStates(function: (T) -> T): Pair<T, T> {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return prevValue to nextValue
        }
    }
}

fun Closeable.closeAndLog(tag: String, title: String) {
    try {
        close()
        Log.i(tag, "$title is closed")
    } catch (e: IOException) {
        Log.w(tag, "Can't close $title: $e")
    }
}
