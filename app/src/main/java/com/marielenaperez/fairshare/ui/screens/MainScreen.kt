package com.marielenaperez.fairshare.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marielenaperez.fairshare.data.Bucket
import com.marielenaperez.fairshare.data.Task
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val buckets by viewModel.buckets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FairShare") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // If no buckets, show message
            if (buckets.isEmpty()) {
                item {
                    Text("No buckets found. Add one!", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                // For each bucket
                items(buckets) { bucket ->
                    // Reuse your composable, but ensure it doesn't do another LazyColumn
                    BucketItem(bucket = bucket, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun BucketItem(bucket: Bucket, viewModel: MainViewModel) {
    val tasks by viewModel.getTasks(bucket.bucketId).collectAsState()

    var newTaskDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Bucket Name
        Text(
            text = bucket.name,
            style = MaterialTheme.typography.titleLarge
        )

        // Existing Tasks
        if (tasks.isEmpty()) {
            Text(
                text = "No tasks in this bucket.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        } else {
            // Simple column for tasks; not another LazyColumn
            tasks.forEach { task ->
                TaskItem(
                    task = task,
                    onDeleteClicked = {
                        viewModel.removeTask(bucket.bucketId, task.taskId)
                    }
                )
            }
        }

        // A row to add a new task
        AddNewTaskRow(
            newTaskDescription = newTaskDescription,
            onTaskDescriptionChange = { newTaskDescription = it },
            onAddTaskClicked = {
                if (newTaskDescription.isNotBlank()) {
                    viewModel.addTask(bucket.bucketId, newTaskDescription.trim())
                    newTaskDescription = ""
                }
            }
        )
    }
}

/**
 * A reusable composable that shows a Spacer, then a row containing:
 *  - an OutlinedTextField to input new tasks
 *  - a Button to add the task
 */
@Composable
fun AddNewTaskRow(
    newTaskDescription: String,
    onTaskDescriptionChange: (String) -> Unit,
    onAddTaskClicked: () -> Unit
) {
    // Spacer before the row
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = newTaskDescription,
            onValueChange = onTaskDescriptionChange,
            label = { Text("New Task") },
            modifier = Modifier.weight(1f)
        )

        Button(onClick = onAddTaskClicked) {
            Text("Add")
        }
    }
}

/**
 * Shows a single task with a delete “X” on the right
 */
@Composable
fun TaskItem(
    task: Task,
    onDeleteClicked: () -> Unit  // <-- Add this parameter
) {
    // A Row so we can place the task text on the left and the "X" button on the right
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Existing text
        Text(
            text = "- ${task.description}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )

        // "X" icon for deleting this task
        IconButton(onClick = onDeleteClicked) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete Task"
            )
        }
    }
}

