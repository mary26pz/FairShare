package com.example.fairshare.data

import androidx.room.Embedded
import androidx.room.Relation

data class BucketWithTasks(
    @Embedded val bucket: Bucket,
    @Relation(
        parentColumn = "bucketId",
        entityColumn = "bucketOwnerId"
    )
    val tasks: List<Task>
)
