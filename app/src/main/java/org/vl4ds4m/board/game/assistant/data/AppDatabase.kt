package org.vl4ds4m.board.game.assistant.data

import android.content.Context
//import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//import androidx.sqlite.db.SupportSQLiteDatabase
import org.vl4ds4m.board.game.assistant.data.dao.SessionDao
import org.vl4ds4m.board.game.assistant.data.entity.GameActionEntity
import org.vl4ds4m.board.game.assistant.data.entity.PlayerEntity
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionEntity
import org.vl4ds4m.board.game.assistant.data.view.SessionInfo

@Database(
    entities = [GameSessionEntity::class, PlayerEntity::class, GameActionEntity::class],
    views = [SessionInfo::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

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
    /*var capturedInstance: AppDatabase? = null
    val initializer = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            capturedInstance?.let {
                prepopulateDatabase(it)
            } ?: throw IllegalStateException(
                "AppDatabase callback is failed as db is not yet built"
            )
        }
    }*/
    return Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "app-database"
    )//.addCallback(initializer)
        .build()
        //.also { capturedInstance = it }
}

/*private fun prepopulateDatabase(db: AppDatabase) {
    Log.i("AppDatabase", "Prepopulate app-database")
    db.clearAllTables()
}*/
