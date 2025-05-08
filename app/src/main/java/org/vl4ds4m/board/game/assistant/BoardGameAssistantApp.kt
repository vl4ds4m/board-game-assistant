package org.vl4ds4m.board.game.assistant

import android.app.Application
import android.net.nsd.NsdManager
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.vl4ds4m.board.game.assistant.data.AppDatabase
import org.vl4ds4m.board.game.assistant.data.repository.GameEnvRepository
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.data.repository.UserDataRepository
import org.vl4ds4m.board.game.assistant.data.userDataStore
import org.vl4ds4m.board.game.assistant.network.SessionEmitter
import org.vl4ds4m.board.game.assistant.network.SessionObserver

class BoardGameAssistantApp : Application() {
    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(applicationContext)
    }

    private val userDataStore: DataStore<Preferences> by lazy {
        applicationContext.userDataStore
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val sessionRepository: GameSessionRepository by lazy {
        Log.i(TAG, "Init GameSessionRepository")
        GameSessionRepository(db.sessionDao(), coroutineScope)
    }

    val gameEnvRepository = GameEnvRepository()

    val userDataRepository: UserDataRepository by lazy {
        Log.i(TAG, "Init UserDataRepository")
        UserDataRepository(userDataStore, coroutineScope)
    }

    val sessionObserver: SessionObserver by lazy {
        val service = applicationContext.getSystemService(NsdManager::class.java)
        SessionObserver(service)
    }

    val sessionEmitter: SessionEmitter by lazy {
        val service = applicationContext.getSystemService(NsdManager::class.java)
        SessionEmitter(service, sessionObserver)
    }

    fun initDependencies() {
        sessionRepository
        userDataRepository.setNetDevId()
        sessionObserver
        sessionEmitter
    }

    companion object {
        fun from(extras: CreationExtras) = extras[APPLICATION_KEY] as BoardGameAssistantApp
    }
}

private const val TAG = "BoardGameAssistantApp"
