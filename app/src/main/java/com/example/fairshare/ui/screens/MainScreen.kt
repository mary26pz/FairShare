package com.example.fairshare.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fairshare.data.Bucket
import com.example.fairshare.data.Task
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    // Observe buckets from ViewModel
    val buckets by viewModel.buckets.collectAsState()

    // Scaffold provides basic material design layout structure
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FairShare") }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
        ) {
            if (buckets.isEmpty()) {
                Text("No buckets found. Add one!", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(buckets) { bucket ->
                        BucketItem(bucket = bucket, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun BucketItem(bucket: Bucket, viewModel: MainViewModel) {
    // Observe tasks from ViewModel
    val tasks by viewModel.getTasks(bucket.bucketId).collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = bucket.name,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(4.dp))

        if (tasks.isEmpty()) {
            Text("No tasks in this bucket.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(task = task)
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Text(
        text = "- ${task.description}",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(start = 16.dp)
    )
}
