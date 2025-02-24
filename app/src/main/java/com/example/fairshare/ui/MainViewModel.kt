package com.example.fairshare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fairshare.data.Bucket
import com.example.fairshare.data.BucketDao
import com.example.fairshare.data.Task
import com.example.fairshare.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bucketDao: BucketDao,
    private val taskDao: TaskDao
) : ViewModel() {

    val buckets = bucketDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getTasks(bucketId: Int) = taskDao.getTasksForBucket(bucketId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addBucket(name: String) {
        viewModelScope.launch {
            bucketDao.insert(Bucket(name = name))
        }
    }

    fun addTask(bucketId: Int, description: String) {
        viewModelScope.launch {
            taskDao.insert(Task(bucketOwnerId = bucketId, description = description))
        }
    }
}
