package org.vl4ds4m.board.game.assistant

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.Closeable
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

val Enum<*>.title: String get() = this.name
    .replaceFirstChar { it.uppercaseChar() }

val String.short: String get() =
    if (length <= 20) this
    else substring(0, 20) + "..."

data class States<T>(val prev: T, val next: T)

inline fun <K, V> MutableStateFlow<Map<K, V>>.updateMap(
    action: MutableMap<K, V>.() -> Unit
): States<Map<K, V>> {
    return updateAndGetStates { it.toMutableMap().apply(action) }
}

inline fun <E> MutableStateFlow<List<E>>.updateList(
    action: MutableList<E>.() -> Unit
): States<List<E>> {
    return updateAndGetStates { it.toMutableList().apply(action) }
}

inline fun <T> MutableStateFlow<T>.updateAndGetStates(function: (T) -> T): States<T> {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return States(prev = prevValue, next = nextValue)
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

val Long.localDateTime: LocalDateTime get() = Instant.ofEpochMilli(this)
    .let { LocalDateTime.ofInstant(it, ZoneId.systemDefault()) }

fun prettyTime(seconds: Int): String {
    val minutes = seconds / 60
    val sec = seconds % 60
    return String.format(
        java.util.Locale.getDefault(),
        "%02d:%02d",
        minutes, sec
    )
}
