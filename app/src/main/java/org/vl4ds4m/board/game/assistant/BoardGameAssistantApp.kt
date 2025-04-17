package org.vl4ds4m.board.game.assistant

import android.app.Application
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.data.AppDatabase
import org.vl4ds4m.board.game.assistant.data.repository.GameEnvRepository
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.data.repository.UserDataRepository
import org.vl4ds4m.board.game.assistant.data.userDataStore
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.PlayerState
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo

class BoardGameAssistantApp : Application() {
    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(applicationContext)
    }

    private val userDataStore: DataStore<Preferences> by lazy {
        applicationContext.userDataStore
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val sessionRepository: GameSessionRepository by lazy {
        GameSessionRepository(db.sessionDao(), coroutineScope).also {
            // Test usage
            coroutineScope.launch {
                db.clearAllTables()
                prepopulateDatabase(it)
            }
        }
    }

    val gameEnvRepository = GameEnvRepository()

    val userDataRepository: UserDataRepository by lazy {
        UserDataRepository(userDataStore, coroutineScope)
    }

    fun initDependencies() {
        sessionRepository
        userDataRepository.setNetDevId()
    }

    companion object {
        fun from(extras: CreationExtras) = extras[APPLICATION_KEY] as BoardGameAssistantApp
    }
}

private fun prepopulateDatabase(repo: GameSessionRepository) {
    Log.i("AppDatabase", "Prepopulate app-database")
    defaultGames.forEachIndexed { i, session ->
        repo.saveSession(session, "local_${i + 1}")
    }
}

private val initialTime: Long = java.time.Instant
    .parse("2025-01-24T10:15:34.00Z").epochSecond


val defaultGames: List<GameSession> = listOf(
    GameSession(
        completed = false,
        type = SimpleOrdered,
        name = "Uno 93",
        players = listOf(
            1L to Player(
                netDevId = null,
                name = "Abc",
                active = true,
                state = PlayerState(120, mapOf())
            ),
            2L to Player(
                netDevId = null,
                name = "Def",
                active = false,
                state = PlayerState(36, mapOf())
            ),
            3L to Player(
                netDevId = null,
                name = "Foo",
                active = true,
                state = PlayerState(154, mapOf())
            )
        ),
        currentPlayerId = 1L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 40_000,
        stopTime = initialTime + 40_005,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0
    ),
    GameSession(
        completed = false,
        type = Free,
        name = "Poker Counts 28",
        players = listOf(
            1L to Player(
                netDevId = null,
                name = "Bar",
                active = true,
                state = PlayerState(1220, mapOf())
            ),
            2L to Player(
                netDevId = null,
                name = "Conf",
                active = true,
                state = PlayerState(376, mapOf())
            ),
            3L to Player(
                netDevId = null,
                name = "Leak",
                active = true,
                state = PlayerState(532, mapOf())
            )
        ),
        currentPlayerId = 2L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 20_000,
        stopTime = initialTime + 20_015,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0
    ),
    GameSession(
        type = SimpleOrdered,
        completed = true,
        name = "Imaginarium 74",
        players = listOf(
            1L to Player(
                netDevId = null,
                name = "Bar",
                active = true,
                state = PlayerState(12, mapOf())
            ),
            2L to Player(
                netDevId = null,
                name = "Conf",
                active = true,
                state = PlayerState(37, mapOf())
            ),
            3L to Player(
                netDevId = null,
                name = "Leak",
                active = true,
                state = PlayerState(53, mapOf())
            ),
            4L to Player(
                netDevId = null,
                name = "Flick",
                active = true,
                state = PlayerState(32, mapOf())
            )
        ),
        currentPlayerId = 3L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 30_000,
        stopTime = initialTime + 30_010,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0
    )
)

val fakeRemoteSession = listOf(
    RemoteSessionInfo("remote_1", "Milki Way", "100.0.0.100", 11234),
    RemoteSessionInfo("remote_2", "Catch me if you can", "100.0.0.100", 11234)
)
