package com.example.fairshare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.data.Bucket
import com.example.fairshare.data.Task
import com.example.fairshare.data.dao.BucketDao
import com.example.fairshare.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bucketDao: BucketDao,
    private val taskDao: TaskDao
) : ViewModel() {

    // ✅ Toggle sample data (Set to false for real DB)
    private val useSampleData = true

    // ✅ Flow to hold bucket list
    private val _buckets = MutableStateFlow<List<Bucket>>(emptyList())
    val buckets: StateFlow<List<Bucket>> = _buckets

    init {
        if (useSampleData) {
            // ✅ Using Hardcoded Sample Data
            _buckets.value = listOf(
                Bucket(bucketId = 1, name = "Work"),
                Bucket(bucketId = 2, name = "Groceries")
            )
        } else {
            // ✅ Fetch Buckets from Database
            viewModelScope.launch(Dispatchers.IO) {
                bucketDao.getAll().collect { bucketList ->
                    _buckets.value = bucketList
                }
            }
        }
    }

    // ✅ Fetches tasks for a specific bucket
    fun getTasks(bucketId: Int): StateFlow<List<Task>> {
        return if (useSampleData) {
            val sampleTasks = mapOf(
                1 to listOf(
                    Task(taskId = 1, bucketOwnerId = 1, description = "Finish project"),
                    Task(taskId = 2, bucketOwnerId = 1, description = "Send email update"),
                    Task(taskId = 3, bucketOwnerId = 1, description = "Submit Hours")
            ),
                2 to listOf(
                    Task(taskId = 3, bucketOwnerId = 2, description = "Buy apples"),
                    Task(taskId = 4, bucketOwnerId = 2, description = "Get milk")
                )
            )
            MutableStateFlow(sampleTasks[bucketId] ?: emptyList())
        } else {
            taskDao.getTasksForBucket(bucketId).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
        }
    }

    // ✅ Ensures database is prepopulated if empty (only for real DB)
    init {
        if (!useSampleData) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val existingBuckets = bucketDao.getAll().firstOrNull()
                    if (existingBuckets.isNullOrEmpty()) {
                        bucketDao.insert(Bucket(name = "Default Bucket"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace() // Prevent crash & log error
                }
            }
        }
    }

    // ✅ Inserts a new bucket safely (only for real DB)
    fun addBucket(name: String) {
        if (!useSampleData) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    bucketDao.insert(Bucket(name = name))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            // ✅ Add sample bucket in-memory
            _buckets.value = _buckets.value + Bucket(bucketId = _buckets.value.size + 1, name = name)
        }
    }

    // ✅ Inserts a task linked to a bucket (only for real DB)
    fun addTask(bucketId: Int, description: String) {
        if (!useSampleData) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    taskDao.insert(Task(bucketOwnerId = bucketId, description = description))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
