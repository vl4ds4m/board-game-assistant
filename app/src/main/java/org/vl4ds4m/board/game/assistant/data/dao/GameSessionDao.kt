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
import org.vl4ds4m.board.game.assistant.data.entity.PlayerData
import org.vl4ds4m.board.game.assistant.data.entity.PlayerEntity
import org.vl4ds4m.board.game.assistant.data.entity.UserEntity
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
        insertPlayersData(data.players)
        insertActions(data.actions)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(entity: GameSessionEntity)

    @Transaction
    suspend fun insertPlayersData(data: List<PlayerData>) {
        val users = data.mapNotNull { it.user }
        insertUsers(users)
        val players = data.map { it.player }
        insertPlayers(players)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(entities: List<UserEntity>)

    @Insert
    suspend fun insertPlayers(entities: List<PlayerEntity>)

    @Insert
    suspend fun insertActions(entities: List<GameActionEntity>)

    @Delete(entity = GameSessionEntity::class)
    suspend fun removeSession(pk: GameSessionEntity.PK)
}
