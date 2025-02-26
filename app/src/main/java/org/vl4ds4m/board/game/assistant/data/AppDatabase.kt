package org.vl4ds4m.board.game.assistant.data

import androidx.room.Database
import androidx.room.RoomDatabase
import org.vl4ds4m.board.game.assistant.data.dao.SessionDao
import org.vl4ds4m.board.game.assistant.data.entity.ActionEntity
import org.vl4ds4m.board.game.assistant.data.entity.PlayerEntity
import org.vl4ds4m.board.game.assistant.data.entity.SessionEntity
import org.vl4ds4m.board.game.assistant.data.view.SessionInfo

@Database(
    entities = [SessionEntity::class, PlayerEntity::class, ActionEntity::class],
    views = [SessionInfo::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
