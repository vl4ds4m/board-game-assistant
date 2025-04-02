package org.vl4ds4m.board.game.assistant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.vl4ds4m.board.game.assistant.data.dao.GameSessionDao
import org.vl4ds4m.board.game.assistant.data.entity.GameActionEntity
import org.vl4ds4m.board.game.assistant.data.entity.PlayerEntity
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionEntity
import org.vl4ds4m.board.game.assistant.data.view.GameSessionInfoView

@Database(
    entities = [
        GameSessionEntity::class,
        PlayerEntity::class,
        GameActionEntity::class
    ],
    views = [GameSessionInfoView::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): GameSessionDao

    companion object {
        @Volatile
        private var mInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return synchronized(this) {
                mInstance ?: build(context.applicationContext).also {
                    mInstance = it
                }
            }
        }
    }
}

private fun build(applicationContext: Context): AppDatabase {
    return Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).build()
}
