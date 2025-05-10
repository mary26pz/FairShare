package com.marielenaperez.fairshare.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marielenaperez.fairshare.data.dao.BucketDao

@Database(entities = [Bucket::class, Task::class], version = 1, exportSchema = true) // Change this to true
abstract class FairShareDatabase : RoomDatabase() {
    abstract fun bucketDao(): BucketDao
    abstract fun taskDao(): TaskDao
}
