package org.vl4ds4m.board.game.assistant.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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
    suspend fun findSession(id: String): GameSessionData?

    @Transaction
    suspend fun insertSession(data: GameSessionData) {
        insertSession(data.entity)
        deletePlayers(getPlayers(data.entity.id))
        insertPlayers(data.players)
        deleteActions(getActions(data.entity.id))
        insertActions(data.actions)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(entity: GameSessionEntity)

    @Query(
        "SELECT * FROM ${PlayerEntity.TABLE_NAME} " +
        "WHERE ${PlayerEntity.SESSION_ID} = :sessionId"
    )
    suspend fun getPlayers(sessionId: String): List<PlayerEntity>

    @Delete
    suspend fun deletePlayers(entities: List<PlayerEntity>)

    @Insert
    suspend fun insertPlayers(entities: List<PlayerEntity>)

    @Query(
        "SELECT * FROM ${GameActionEntity.TABLE_NAME} " +
        "WHERE ${GameActionEntity.SESSION_ID} = :sessionId"
    )
    suspend fun getActions(sessionId: String): List<GameActionEntity>

    @Delete
    suspend fun deleteActions(entities: List<GameActionEntity>)

    @Insert
    suspend fun insertActions(entities: List<GameActionEntity>)
}
