package com.example.fairshare.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Bucket::class,
            parentColumns = ["bucketId"],
            childColumns = ["bucketOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bucketOwnerId"])] // Add this line
)
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val bucketOwnerId: Int,
    val description: String,
    val timestamp: Long = System.currentTimeMillis() // Add this field
)
