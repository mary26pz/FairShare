package com.marielenaperez.fairshare.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.marielenaperez.fairshare.data.Bucket
import com.marielenaperez.fairshare.data.BucketWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface BucketDao {

    // ✅ Retrieve all buckets, sorted by newest first
    @Query("SELECT * FROM buckets ORDER BY bucketId DESC")
    fun getAll(): Flow<List<Bucket>>

    // ✅ Insert bucket, replacing existing if conflict occurs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bucket: Bucket)

    // ✅ Retrieve a bucket along with its tasks
    @Transaction
    @Query("SELECT * FROM buckets WHERE bucketId = :bucketId")
    suspend fun getBucketWithTasks(bucketId: Int): List<BucketWithTasks>
}
