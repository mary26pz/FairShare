package com.example.fairshare.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = Bucket::class,
        parentColumns = ["bucketId"],
        childColumns = ["bucketOwnerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val bucketOwnerId: Int,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isReviewed: Boolean = false,
    val reviewedTimestamp: Long? = null
)
