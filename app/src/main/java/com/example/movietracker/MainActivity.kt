package com.example.movietracker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val testButton: Button = findViewById(R.id.testFirebaseButton)
        testButton.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val testMovie = hashMapOf(
                "title" to "Test Movie",
                "release_date" to "2024"
            )

            db.collection("movies")
                .add(testMovie)
                .addOnSuccessListener { documentReference ->
                    Log.d("FirebaseTest", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("FirebaseTest", "Error adding document", e)
                }
        }
    }
}