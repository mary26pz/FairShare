import com.marielenaperez.fairshare.data.Bucket
import com.marielenaperez.fairshare.data.Task
import com.marielenaperez.fairshare.data.BucketWithTasks

val sampleTasks = listOf(
    Task(taskId = 1, bucketOwnerId = 1, description = "Sample Task 1"),
    Task(taskId = 2, bucketOwnerId = 1, description = "Sample Task 2")
)

val sampleBuckets = listOf(
    BucketWithTasks(bucket = Bucket(bucketId = 1, name = "Sample Bucket 1"), tasks = sampleTasks),
    BucketWithTasks(bucket = Bucket(bucketId = 2, name = "Sample Bucket 2"), tasks = sampleTasks)
)
