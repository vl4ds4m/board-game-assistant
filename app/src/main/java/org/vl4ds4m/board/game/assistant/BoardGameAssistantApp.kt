package org.vl4ds4m.board.game.assistant

import android.app.Application
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.vl4ds4m.board.game.assistant.data.AppDatabase
import org.vl4ds4m.board.game.assistant.data.repository.GameRepository
import org.vl4ds4m.board.game.assistant.data.repository.GameSessionRepository
import org.vl4ds4m.board.game.assistant.data.repository.UserDataRepository
import org.vl4ds4m.board.game.assistant.data.userDataStore
import org.vl4ds4m.board.game.assistant.game.Free
import org.vl4ds4m.board.game.assistant.game.Player
import org.vl4ds4m.board.game.assistant.game.SimpleOrdered
import org.vl4ds4m.board.game.assistant.game.data.GameSession
import org.vl4ds4m.board.game.assistant.game.data.Score
import org.vl4ds4m.board.game.assistant.game.simple.SimpleOrderedGameState
import org.vl4ds4m.board.game.assistant.network.RemoteSession

class BoardGameAssistantApp : Application() {
    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(applicationContext)
    }

    private val userDataStore: DataStore<Preferences> by lazy {
        applicationContext.userDataStore
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val sessionRepository: GameSessionRepository by lazy {
        GameSessionRepository(db.sessionDao(), db.sessionNextIdDao(), coroutineScope).also {
            // Test usage
            coroutineScope.launch {
                db.clearAllTables()
                prepopulateDatabase(it)
            }
        }
    }

    val gameRepository = GameRepository()

    val userDataRepository by lazy { UserDataRepository(userDataStore, coroutineScope) }
}

private fun prepopulateDatabase(repo: GameSessionRepository) {
    Log.i("AppDatabase", "Prepopulate app-database")
    defaultGames.forEach {
        repo.saveSession(it)
    }
}

private val initialTime: Long = java.time.Instant
    .parse("2025-01-24T10:15:34.00Z").epochSecond


val defaultGames: List<GameSession> = listOf(
    GameSession(
        completed = false,
        type = SimpleOrdered,
        name = "Uno 93",
        players = mapOf(
            1L to Player(
                name = "Abc",
                active = true,
                state = Score(120)
            ),
            2L to Player(
                name = "Def",
                active = false,
                state = Score(36)
            ),
            3L to Player(
                name = "Foo",
                active = true,
                state = Score(154)
            )
        ),
        currentPlayerId = 1L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 40_000,
        stopTime = initialTime + 40_005,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0,
        additional = SimpleOrderedGameState(listOf(1, 2, 3))
    ),
    GameSession(
        completed = false,
        type = Free,
        name = "Poker Counts 28",
        players = mapOf(
            1L to Player(
                name = "Bar",
                active = true,
                state = Score(1220)
            ),
            2L to Player(
                name = "Conf",
                active = true,
                state = Score(376)
            ),
            3L to Player(
                name = "Leak",
                active = true,
                state = Score(532)
            )
        ),
        currentPlayerId = 2L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 20_000,
        stopTime = initialTime + 20_015,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0,
        additional = null
    ),
    GameSession(
        type = SimpleOrdered,
        completed = true,
        name = "Imaginarium 74",
        players = mapOf(
            1L to Player(
                name = "Bar",
                active = true,
                state = Score(12)
            ),
            2L to Player(
                name = "Conf",
                active = true,
                state = Score(37)
            ),
            3L to Player(
                name = "Leak",
                active = true,
                state = Score(53)
            ),
            4L to Player(
                name = "Flick",
                active = true,
                state = Score(32)
            )
        ),
        currentPlayerId = 3L,
        nextNewPlayerId = 10L,
        startTime = initialTime + 30_000,
        stopTime = initialTime + 30_010,
        timeout = false,
        secondsUntilEnd = 0,
        actions = listOf(),
        currentActionPosition = 0,
        additional = SimpleOrderedGameState(listOf(1, 2, 3, 4))
    )
)

val fakeRemoteSession = listOf(
    RemoteSession(1, "Milki Way", "100.0.0.100", 11234),
    RemoteSession(2, "Catch me if you can", "100.0.0.100", 11234)
)
