package org.vl4ds4m.board.game.assistant

import android.app.Application
import androidx.room.Room
import org.vl4ds4m.board.game.assistant.data.AppDatabase
import org.vl4ds4m.board.game.assistant.data.repository.SessionRepository

class BoardGameAssistantApp : Application() {
    private val db: AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "app-database"
    ).build()

    val sessionRepository: SessionRepository = SessionRepository(db.sessionDao())
}
