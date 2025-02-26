package org.vl4ds4m.board.game.assistant.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.vl4ds4m.board.game.assistant.data.entity.SessionEntity
import org.vl4ds4m.board.game.assistant.data.view.SessionInfo

@Dao
interface SessionDao {
    @Query("SELECT * FROM ${SessionInfo.VIEW_NAME}")
    fun getAllSessions(): List<SessionInfo>

    @Query("SELECT * FROM ${SessionEntity.TABLE_NAME} WHERE ${SessionEntity.ID} = :id")
    fun findSession(id: Long): SessionEntity?

    @Insert
    fun insertSession(session: SessionEntity)

    @Update
    fun updateSession(session: SessionEntity)
}
