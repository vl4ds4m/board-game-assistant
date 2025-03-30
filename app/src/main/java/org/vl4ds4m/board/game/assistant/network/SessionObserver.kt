package org.vl4ds4m.board.game.assistant.network

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vl4ds4m.board.game.assistant.fakeRemoteSession

class SessionObserver {
    val sessions: StateFlow<List<RemoteSession>> = MutableStateFlow(fakeRemoteSession)

    suspend fun observe() {
        delay(1000)
        Log.i("SessionObserver", "Not implemented")
    }
}
