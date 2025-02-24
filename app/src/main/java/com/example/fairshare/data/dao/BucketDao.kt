package com.example.fairshare.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BucketDao {
    @Insert
    suspend fun insert(bucket: Bucket)

    @Query("SELECT * FROM buckets")
    fun getAll(): Flow<List<Bucket>>
}
