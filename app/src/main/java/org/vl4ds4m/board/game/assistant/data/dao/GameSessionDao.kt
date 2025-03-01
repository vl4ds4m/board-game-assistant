package org.vl4ds4m.board.game.assistant.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.vl4ds4m.board.game.assistant.data.entity.GameActionEntity
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionData
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionEntity
import org.vl4ds4m.board.game.assistant.data.entity.PlayerEntity
import org.vl4ds4m.board.game.assistant.data.view.GameSessionInfoView

@Dao
interface GameSessionDao {
    @Query("SELECT * FROM ${GameSessionInfoView.VIEW_NAME}")
    fun getAllSessions(): Flow<List<GameSessionInfoView>>

    @Transaction
    @Query("SELECT * FROM ${GameSessionEntity.TABLE_NAME} WHERE ${GameSessionEntity.ID} = :id")
    suspend fun findSession(id: Long): GameSessionData?

    @Transaction
    suspend fun insertSession(data: GameSessionData) {
        insertSession(data.entity)
        insertPlayers(data.players)
        insertActions(data.actions)
    }

    @Insert
    suspend fun insertSession(entity: GameSessionEntity)

    @Insert
    suspend fun insertPlayers(entities: List<PlayerEntity>)

    @Insert
    suspend fun insertActions(entities: List<GameActionEntity>)

    @Transaction
    suspend fun updateSession(data: GameSessionData) {
        updateSession(data.entity)
        deletePlayers(getPlayers(data.entity.id))
        insertPlayers(data.players)
        deleteActions(getActions(data.entity.id))
        insertActions(data.actions)
    }

    @Update
    suspend fun updateSession(entity: GameSessionEntity)

    @Query(
        "SELECT * FROM ${PlayerEntity.TABLE_NAME} " +
        "WHERE ${PlayerEntity.SESSION_ID} = :sessionId"
    )
    suspend fun getPlayers(sessionId: Long): List<PlayerEntity>

    @Delete
    suspend fun deletePlayers(entities: List<PlayerEntity>)

    @Query(
        "SELECT * FROM ${GameActionEntity.TABLE_NAME} " +
        "WHERE ${GameActionEntity.SESSION_ID} = :sessionId"
    )
    suspend fun getActions(sessionId: Long): List<GameActionEntity>

    @Delete
    suspend fun deleteActions(entities: List<GameActionEntity>)
}
