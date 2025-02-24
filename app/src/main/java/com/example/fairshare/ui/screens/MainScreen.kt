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
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    // Collect the list of buckets from the ViewModel
    val buckets = viewModel.buckets.collectAsState().value

    // Scaffold provides basic material design layout structure
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FairShare") }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
            ) {
                items(buckets) { bucket ->
                    BucketItem(bucket = bucket, viewModel = viewModel)
                }
            }
        }
    )
}

@Composable
fun BucketItem(bucket: Bucket, viewModel: MainViewModel) {
    // Collect the list of tasks for the given bucket
    val tasks = viewModel.getTasks(bucket.bucketId).collectAsState().value

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
        tasks.forEach { task ->
            TaskItem(task = task)
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Text(
        text = task.description,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(start = 16.dp)
    )
}
