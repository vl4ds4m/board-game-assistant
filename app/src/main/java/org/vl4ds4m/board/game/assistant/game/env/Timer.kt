package org.vl4ds4m.board.game.assistant.game.env

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.game.Initializable
import kotlin.time.Duration.Companion.seconds

class Timer : Initializable {
    private var scope: CoroutineScope? = null
    private var job: Job? = null

    override fun init(scope: CoroutineScope) {
        stop()
        this.scope = scope
    }

    override fun close() {
        stop()
        job = null
        this.scope = null
    }

    fun start(
        secondsToEnd: MutableStateFlow<Int>,
        completed: MutableStateFlow<Boolean>
    ) {
        stop()
        job = scope?.launch {
            var timeout = false
            while (!timeout) {
                secondsToEnd.update { seconds ->
                    if (seconds > 0) {
                        delay(1.seconds)
                        seconds - 1
                    } else {
                        timeout = true
                        0
                    }
                }
            }
            completed.value = true
        }
    }

    fun stop() {
        job?.cancel()
    }
}
