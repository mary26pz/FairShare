package com.example.fairshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.fairshare.ui.MainScreen
import com.example.fairshare.ui.MainViewModel
import com.example.fairshare.ui.theme.FairShareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FairShareTheme {
                MainScreen(viewModel = mainViewModel)
            }
        }
    }
}
