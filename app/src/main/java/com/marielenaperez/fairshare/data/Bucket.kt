package com.marielenaperez.fairshare.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buckets")
data class Bucket(
    @PrimaryKey(autoGenerate = true)
    val bucketId: Int = 0,
    val name: String = ""
) {
    // No-argument constructor required for Firestore
    constructor() : this(0, "")  // Default values for Firestore deserialization
}
