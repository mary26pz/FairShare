package com.marielenaperez.fairshare

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.firebase.analytics.FirebaseAnalytics
import com.marielenaperez.fairshare.ui.MainScreen
import com.marielenaperez.fairshare.ui.MainViewModel
import com.marielenaperez.fairshare.ui.theme.FairShareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    // Firebase Analytics instance
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Log a custom event to Firebase Analytics
        logAnalyticsEvent()

        setContent {
            FairShareTheme {
                MainScreen(viewModel = mainViewModel)
            }
        }
    }

    // Function to log an event
    private fun logAnalyticsEvent() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity Loaded")
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        Log.d("FirebaseAnalytics", "Logged MainActivity Loaded event")
    }
}
