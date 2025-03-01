package org.vl4ds4m.board.game.assistant.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import org.vl4ds4m.board.game.assistant.data.entity.GameSessionNextId

@Dao
interface GameSessionNextIdDao {
    @Transaction
    suspend fun getAndIncrementSessionId(): Long = findNextId().let {
        val id = it ?: GameSessionNextId.FIRST_VALUE
        val nid = GameSessionNextId(id + 1)
        if (it == null) insertNextId(nid)
        else updateNextId(nid)
        id
    }

    @Query(
        "SELECT ${GameSessionNextId.VALUE} " +
        "FROM ${GameSessionNextId.TABLE_NAME} " +
        "WHERE ${GameSessionNextId.KEY} = ${GameSessionNextId.SINGLE_KEY}"
    )
    suspend fun findNextId(): Long?

    @Insert
    suspend fun insertNextId(nextId: GameSessionNextId)

    @Update
    suspend fun updateNextId(nextId: GameSessionNextId)
}
