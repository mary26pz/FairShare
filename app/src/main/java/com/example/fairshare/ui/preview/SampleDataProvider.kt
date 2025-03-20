package com.example.fairshare.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.fairshare.data.Bucket
import com.example.fairshare.data.Task
import com.example.fairshare.data.BucketWithTasks

class SampleBucketProvider : PreviewParameterProvider<BucketWithTasks> {
    override val values = sequenceOf(
        BucketWithTasks(
            bucket = Bucket(bucketId = 1, name = "Sample Bucket 1"),
            tasks = listOf(Task(taskId = 1, bucketOwnerId = 1, description = "Sample Task"))
        ),
        BucketWithTasks(
            bucket = Bucket(bucketId = 2, name = "Sample Bucket 2"),
            tasks = listOf(Task(taskId = 2, bucketOwnerId = 2, description = "Another Task"))
        )
    )
}
