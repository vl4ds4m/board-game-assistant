package org.vl4ds4m.board.game.assistant.domain

import kotlinx.coroutines.CoroutineScope

interface Initializable : AutoCloseable {
    fun init(scope: CoroutineScope)
}
