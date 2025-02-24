package com.example.fairshare.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buckets")
data class Bucket(
    @PrimaryKey(autoGenerate = true) val bucketId: Int = 0,
    val name: String
)
