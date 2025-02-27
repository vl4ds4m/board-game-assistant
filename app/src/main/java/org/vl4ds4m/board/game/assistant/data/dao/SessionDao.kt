package org.vl4ds4m.board.game.assistant.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionEntity
import org.vl4ds4m.board.game.assistant.data.view.SessionInfo

@Dao
interface SessionDao {
    @Query("SELECT * FROM ${SessionInfo.VIEW_NAME}")
    fun getAllSessions(): List<SessionInfo>

    @Query("SELECT * FROM ${GameSessionEntity.TABLE_NAME} WHERE ${GameSessionEntity.ID} = :id")
    fun findSession(id: Long): GameSessionEntity?

    @Insert
    fun insertSession(session: GameSessionEntity)

    @Update
    fun updateSession(session: GameSessionEntity)
}
