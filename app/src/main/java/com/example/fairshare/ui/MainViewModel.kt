package com.example.fairshare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.data.Bucket
import com.example.fairshare.data.dao.BucketDao
import com.example.fairshare.data.Task
import com.example.fairshare.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bucketDao: BucketDao,
    private val taskDao: TaskDao
) : ViewModel() {

    // ✅ Fetches all buckets and sorts by createdAt (or bucketId as fallback)
    val buckets = bucketDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // ✅ Fetches tasks for a specific bucket
    fun getTasks(bucketId: Int) = taskDao.getTasksForBucket(bucketId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // ✅ Ensures database is prepopulated with a default bucket if empty
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val existingBuckets = bucketDao.getAll().first()
            if (existingBuckets.isEmpty()) {
                bucketDao.insert(Bucket(name = "Default Bucket"))
            }
        }
    }

    // ✅ Inserts a new bucket safely
    fun addBucket(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            bucketDao.insert(Bucket(name = name))
        }
    }

    // ✅ Inserts a task linked to a bucket
    fun addTask(bucketId: Int, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insert(Task(bucketOwnerId = bucketId, description = description))
        }
    }
}
