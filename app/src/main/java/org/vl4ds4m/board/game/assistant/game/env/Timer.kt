package org.vl4ds4m.board.game.assistant.game.env

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        enabled: StateFlow<Boolean>,
        secondsToEnd: MutableStateFlow<Int>,
        onTimeout: () -> Unit
    ) {
        stop()
        job = scope?.launch {
            var timer: Job? = null
            enabled.collect { on ->
                timer?.cancel()
                if (on) {
                    timer = launch {
                        startCountdown(secondsToEnd, onTimeout)
                    }
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
    }
}

private suspend fun startCountdown(
    secondsToEnd: MutableStateFlow<Int>,
    onTimeout: () -> Unit
) {
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
    onTimeout()
}
