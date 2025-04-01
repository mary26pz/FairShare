package com.example.fairshare.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fairshare.data.Bucket
import com.example.fairshare.data.Task

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

    // Just a Column — no vertical scroll
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = bucket.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )

        if (tasks.isEmpty()) {
            Text(
                text = "No tasks in this bucket.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        } else {
            // Simple column for tasks; not another LazyColumn
            tasks.forEach { task ->
                TaskItem(task = task)
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Text(
        text = "- ${task.description}",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
    )
}

