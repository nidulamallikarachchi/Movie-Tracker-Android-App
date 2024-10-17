package com.example.movietracker.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.movietracker.R
import com.example.movietracker.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class DisplayUserProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var profileImage: ImageView
    private lateinit var textFirstName: TextView
    private lateinit var textUsername: TextView
    private lateinit var textAboutMe: TextView
    private lateinit var textMoviePreferences: TextView
    private val userId = "user123" // Replace with actual user ID passed from the previous activity

    private lateinit var buttonEditProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_user_profile)

        firestore = FirebaseFirestore.getInstance()

        profileImage = findViewById(R.id.imageViewProfile)
        textFirstName = findViewById(R.id.textFirstName)
        textUsername = findViewById(R.id.textUsername)
        textAboutMe = findViewById(R.id.textAboutMe)
        textMoviePreferences = findViewById(R.id.textMoviePreferences) // Add this line to link your TextView
        buttonEditProfile = findViewById(R.id.buttonEditProfile)

        // Get user ID from the intent
        val userIdFromIntent = intent.getStringExtra("USER_ID") ?: userId

        // Load the user profile
        loadUserProfile(userIdFromIntent)

        buttonEditProfile.setOnClickListener {
            val intent = Intent(this, EditUserProfileActivity::class.java)
            intent.putExtra("USER_ID", userIdFromIntent) // Pass user ID to edit activity
            startActivity(intent)
        }
    }

    private fun loadUserProfile(userId: String) {
        val userProfileRef = firestore.collection("userProfiles").document(userId)

        userProfileRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val userProfile = document.toObject(UserProfile::class.java)
                userProfile?.let {
                    textFirstName.text = it.firstName
                    textUsername.text = it.username
                    textAboutMe.text = it.aboutMe
                    // Load the profile picture into ImageView using Picasso
                    if (it.profilePicUrl.isNotEmpty()) {
                        Picasso.get().load(it.profilePicUrl).into(profileImage)
                    } else {
                        profileImage.setImageResource(R.drawable.placeholder_image) // Set a placeholder image
                    }
                    // Display movie preferences
                    textMoviePreferences.text = it.moviePreferences.joinToString(", ") // Display preferences as a comma-separated string
                }
            } else {
                Log.d("DisplayUserProfileActivity", "No such document")
            }
        }.addOnFailureListener { e ->
            Log.e("DisplayUserProfileActivity", "Error loading user profile: ${e.message}", e)
        }
    }
}
