package com.marielenaperez.fairshare.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Query("SELECT * FROM tasks WHERE bucketOwnerId = :bucketId ORDER BY timestamp DESC")
    fun getTasksForBucket(bucketId: Int): Flow<List<Task>>
}
