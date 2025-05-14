package com.marielenaperez.fairshare

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.marielenaperez.fairshare.ui.MainScreen
import com.marielenaperez.fairshare.ui.MainViewModel
import com.marielenaperez.fairshare.ui.theme.FairShareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    // Firebase Firestore instance
    private lateinit var db: FirebaseFirestore

    // Get the FirebaseAuth instance
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firestore
        db = Firebase.firestore

        // Authenticate the user first
        authenticateUser()

        setContent {
            FairShareTheme {
                MainScreen(viewModel = mainViewModel)
            }
        }
    }

    // In your onCreate or on a specific event (e.g., button click)
    fun authenticateUser() {
        val email = "user@example.com"
        val password = "userpassword"

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseAuth", "signInWithEmail:success")
                    val user = auth.currentUser
                    // Now you can proceed with saving data to Firestore
                    saveDataToFirestore()
                } else {
                    Log.w("FirebaseAuth", "signInWithEmail:failure", task.exception)
                    // Handle sign-in failure (e.g., show error message)
                }
            }
    }

    private fun saveDataToFirestore() {
        val user = hashMapOf(
            "first_name" to "John",
            "last_name" to "Doe",
            "age" to 29
        )

        // Ensure the user is authenticated before saving data
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is authenticated, save data to Firestore
            db.collection("users")
                .document(currentUser.uid)  // Use the user's UID as the document ID
                .set(user)  // Use `set()` instead of `add()`
                .addOnSuccessListener {
                    Log.d("FirebaseFirestore", "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    Log.w("FirebaseFirestore", "Error adding document", e)
                }
        } else {
            Log.w("FirebaseAuth", "User is not authenticated")
            // Handle the case where the user is not authenticated
        }
    }
}
