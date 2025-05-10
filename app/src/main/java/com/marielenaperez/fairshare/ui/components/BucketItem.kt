package com.marielenaperez.fairshare.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.marielenaperez.fairshare.data.Bucket
import com.marielenaperez.fairshare.ui.preview.SampleBucketProvider

@Composable
fun BucketItem(bucket: Bucket) {
    Text(text = bucket.name)
}

@Preview(showBackground = true)
@Composable
fun PreviewBucketItem(
    @PreviewParameter(SampleBucketProvider::class) bucket: Bucket
) {
    BucketItem(bucket = bucket)
}
