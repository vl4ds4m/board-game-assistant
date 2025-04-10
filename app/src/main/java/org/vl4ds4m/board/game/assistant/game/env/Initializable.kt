package org.vl4ds4m.board.game.assistant.game.env

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface Initializable : AutoCloseable {
    fun init(scope: CoroutineScope)
}

fun Initializable(initializer: (CoroutineScope) -> Job): Initializable =
    InitializableJob(initializer)

private class InitializableJob(
    private val initializer: (CoroutineScope) -> Job
) : Initializable {
    private var job: Job? = null

    override fun init(scope: CoroutineScope) {
        close()
        job = initializer.invoke(scope)
    }

    override fun close() {
        job?.cancel()
        job = null
    }
}
