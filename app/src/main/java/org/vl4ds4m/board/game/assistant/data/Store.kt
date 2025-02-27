package org.vl4ds4m.board.game.assistant.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.env.GameEnv
import java.util.concurrent.atomic.AtomicLong

object Store {
    private val mSessions = mutableStateMapOf<Long, GameSession>()
    private var nextSessionId: AtomicLong = AtomicLong(0)

    var currentGameEnv: GameEnv? = null

    val sessions: Map<Long, GameSession> = mSessions

    fun save(session: GameSession, id: Long? = null) {
        val sessionId = when (id) {
            null -> this.nextSessionId.incrementAndGet()
            in mSessions -> id
            else -> return
        }
        mSessions[sessionId] = session
    }

    fun load(sessionId: Long): GameSession? {
        return sessions[sessionId]
    }

    val username: MutableState<String> = mutableStateOf("A. Helper")
}
