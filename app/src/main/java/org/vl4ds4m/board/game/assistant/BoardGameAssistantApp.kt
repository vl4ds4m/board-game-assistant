package org.vl4ds4m.board.game.assistant

import android.app.Application
import androidx.room.Room
import org.vl4ds4m.board.game.assistant.data.AppDatabase

class BoardGameAssistantApp : Application() {
    val db: AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "app-database"
    ).build()
}
