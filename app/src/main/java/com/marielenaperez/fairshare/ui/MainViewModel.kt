package com.marielenaperez.fairshare.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.marielenaperez.fairshare.data.Bucket
import com.marielenaperez.fairshare.data.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore // Firebase Firestore instance
) : ViewModel() {

    private val _buckets = MutableStateFlow<List<Bucket>>(emptyList())
    val buckets: StateFlow<List<Bucket>> = _buckets

    init {
        // Fetch buckets from Firestore
        viewModelScope.launch(Dispatchers.IO) {
            firebaseFirestore.collection("buckets")
                .get()
                .addOnSuccessListener { snapshot ->
                    val bucketList = snapshot.documents.mapNotNull { it.toObject<Bucket>() }
                    _buckets.value = bucketList
                }
                .addOnFailureListener {
                    Log.w("Firebase", "Error getting buckets.", it)
                }
        }
    }

    fun addBucket(name: String) {
        val newBucket = Bucket(name = name)

        // Save to Firestore
        viewModelScope.launch(Dispatchers.IO) {
            firebaseFirestore.collection("buckets")
                .add(newBucket)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firebase", "Bucket added with ID: ${documentReference.id}")
                    // Optionally, you could fetch updated data or add it to the current state
                }
                .addOnFailureListener { e ->
                    Log.w("Firebase", "Error adding bucket", e)
                }
        }
    }

    fun getTasks(bucketId: String): StateFlow<List<Task>> {
        val tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())

        // Fetch tasks from Firestore (for a specific bucket)
        viewModelScope.launch(Dispatchers.IO) {
            firebaseFirestore.collection("buckets")
                .document(bucketId)
                .collection("tasks")
                .get()
                .addOnSuccessListener { snapshot ->
                    val taskList = snapshot.documents.mapNotNull { it.toObject<Task>() }
                    tasksStateFlow.value = taskList
                }
                .addOnFailureListener {
                    Log.w("Firebase", "Error getting tasks.", it)
                }
        }

        return tasksStateFlow
    }

    fun addTask(bucketId: Int, description: String) {  // Change bucketId to Int
        val newTask = Task(
            description = description,
            bucketOwnerId = bucketId // Pass bucketId as Int
        )

        // Add new task to Firestore under the correct bucket's tasks collection
        viewModelScope.launch(Dispatchers.IO) {
            firebaseFirestore.collection("buckets")
                .document(bucketId.toString())  // Convert Int to String for Firestore
                .collection("tasks")
                .add(newTask)
                .addOnSuccessListener {
                    Log.d("Firebase", "Task added to bucket: $bucketId")
                }
                .addOnFailureListener { e ->
                    Log.w("Firebase", "Error adding task", e)
                }
        }
    }



    fun removeTask(bucketId: String, taskId: String) {
        // Delete task from Firestore
        viewModelScope.launch(Dispatchers.IO) {
            firebaseFirestore.collection("buckets")
                .document(bucketId)
                .collection("tasks")
                .document(taskId)
                .delete()
                .addOnSuccessListener {
                    Log.d("Firebase", "Task removed from bucket: $bucketId")
                }
                .addOnFailureListener { e ->
                    Log.w("Firebase", "Error removing task", e)
                }
        }
    }
}
