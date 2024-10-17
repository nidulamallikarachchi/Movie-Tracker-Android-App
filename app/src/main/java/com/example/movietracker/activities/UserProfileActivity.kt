package com.example.movietracker.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movietracker.R
import com.example.movietracker.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var profileImage: ImageView
    private lateinit var editFirstName: EditText
    private lateinit var editUsername: EditText
    private lateinit var editAboutMe: EditText
    private lateinit var buttonSaveProfile: Button
    private val userId = "user123" // Replace with actual user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        firestore = FirebaseFirestore.getInstance()

        profileImage = findViewById(R.id.profileImage)
        editFirstName = findViewById(R.id.editFirstName)
        editUsername = findViewById(R.id.editUsername)
        editAboutMe = findViewById(R.id.editAboutMe)
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile)

        // Load the user profile if it exists
        loadUserProfile()

        // Save button click listener
        buttonSaveProfile.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun loadUserProfile() {
        val userProfileRef = firestore.collection("userProfiles").document(userId)

        userProfileRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val userProfile = document.toObject(UserProfile::class.java)
                userProfile?.let {
                    editFirstName.setText(it.firstName)
                    editUsername.setText(it.username)
                    editAboutMe.setText(it.aboutMe)
                    // Load profile picture here using Glide or any other image loading library
                    // Glide.with(this).load(it.profilePicId).into(profileImage)
                }
            } else {
                Log.d("UserProfileActivity", "No such document")
            }
        }.addOnFailureListener { e ->
            Log.e("UserProfileActivity", "Error loading user profile: ${e.message}", e)
        }
    }

    private fun saveUserProfile() {
        val userProfile = UserProfile(
            userId = userId,
            firstName = editFirstName.text.toString(),
            username = editUsername.text.toString(),
            aboutMe = editAboutMe.text.toString(),
            profilePicId = "", // Update with actual profile picture URL if applicable
            moviePreferences = listOf() // Replace with actual preferences logic
        )

        val userProfileRef = firestore.collection("userProfiles").document(userId)
        userProfileRef.set(userProfile)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                Log.d("UserProfileActivity", "User profile updated: ${userProfile.username}")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                Log.e("UserProfileActivity", "Error updating profile: ${e.message}", e)
            }
    }
}
