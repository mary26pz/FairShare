package com.example.fairshare.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bucket::class, Task::class], version = 1)
abstract class FairShareDatabase : RoomDatabase() {
    abstract fun bucketDao(): BucketDao
    abstract fun taskDao(): TaskDao
}
