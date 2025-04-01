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

    // Toggle this to false when you want to use real DB
    private val useSampleData = true

    // Flow to hold bucket list
    private val _buckets = MutableStateFlow<List<Bucket>>(emptyList())
    val buckets: StateFlow<List<Bucket>> = _buckets

    // In-memory storage of tasks for sample mode: bucketId -> list of tasks
    private val _sampleTasks = MutableStateFlow(
        mapOf(
            1 to listOf(
                Task(taskId = 1, bucketOwnerId = 1, description = "Fed Alice"),
                Task(taskId = 2, bucketOwnerId = 1, description = "Took out trash"),
                Task(taskId = 3, bucketOwnerId = 1, description = "Changed toilet paper")
            ),
            2 to listOf(
                Task(taskId = 4, bucketOwnerId = 2, description = "Cleaned dishes"),
                Task(taskId = 5, bucketOwnerId = 2, description = "Changed Bounty")
            )
        )
    )

    init {
        if (useSampleData) {
            // Sample Buckets
            _buckets.value = listOf(
                Bucket(bucketId = 1, name = "Alexis"),
                Bucket(bucketId = 2, name = "Mary")
            )
        } else {
            // Real DB: observe all buckets
            viewModelScope.launch(Dispatchers.IO) {
                bucketDao.getAll().collect { bucketList ->
                    _buckets.value = bucketList
                }
            }
        }
    }

    /**
     * Returns a StateFlow of tasks for the given bucketId.
     * - In sample mode, we transform _sampleTasks to pick the correct list
     * - In real DB mode, we grab the flow from taskDao
     */
    fun getTasks(bucketId: Int): StateFlow<List<Task>> {
        return if (useSampleData) {
            // Convert _sampleTasks map into a list for this specific bucket
            _sampleTasks
                .map { tasksMap -> tasksMap[bucketId] ?: emptyList() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                )
        } else {
            // Real DB
            taskDao.getTasksForBucket(bucketId)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                )
        }
    }

    /**
     * Adds a new bucket. In sample mode, add it in memory.
     * In real DB mode, insert via bucketDao.
     */
    fun addBucket(name: String) {
        if (!useSampleData) {
            // Real DB
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    bucketDao.insert(Bucket(name = name))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            // Sample mode: create a new bucket in memory
            val newId = (_buckets.value.maxOfOrNull { it.bucketId } ?: 0) + 1
            val newBucket = Bucket(bucketId = newId, name = name)
            _buckets.value = _buckets.value + newBucket

            // Also add an empty list of tasks for the new bucket
            val updatedMap = _sampleTasks.value.toMutableMap()
            updatedMap[newId] = emptyList()
            _sampleTasks.value = updatedMap
        }
    }

    /**
     * Adds a new task to a given bucket. If sample mode, update _sampleTasks map.
     * If real DB, insert into the database.
     */
    fun addTask(bucketId: Int, description: String) {
        if (!useSampleData) {
            // Real DB insertion
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    taskDao.insert(Task(bucketOwnerId = bucketId, description = description))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            // Sample mode: Add a task to the in-memory map
            val currentTasks = _sampleTasks.value[bucketId].orEmpty()
            val newTaskId = (currentTasks.maxOfOrNull { it.taskId } ?: 0) + 1
            val newTask = Task(
                taskId = newTaskId,
                bucketOwnerId = bucketId,
                description = description
            )
            // Update the map with the new task
            val updatedMap = _sampleTasks.value.toMutableMap()
            updatedMap[bucketId] = currentTasks + newTask
            _sampleTasks.value = updatedMap
        }
    }

    /**
     * Removes a task from a given bucket (sample mode).
     * If real DB, you'd need a delete query in TaskDao (commented out below).
     */
    fun removeTask(bucketId: Int, taskId: Int) {
        if (!useSampleData) {
            // For real DB usage, you'd do something like:
            // viewModelScope.launch(Dispatchers.IO) {
            //     taskDao.deleteTaskById(taskId)
            // }
            return
        }

        // Sample mode: remove task from our in-memory map
        val currentMap = _sampleTasks.value
        val currentTasks = currentMap[bucketId].orEmpty()
        val updatedTasks = currentTasks.filterNot { it.taskId == taskId }

        val updatedMap = currentMap.toMutableMap()
        updatedMap[bucketId] = updatedTasks
        _sampleTasks.value = updatedMap
    }

    // Init block to ensure DB is prepopulated if empty (only for real DB)
    init {
        if (!useSampleData) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val existingBuckets = bucketDao.getAll().firstOrNull()
                    if (existingBuckets.isNullOrEmpty()) {
                        bucketDao.insert(Bucket(name = "Default Bucket"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
