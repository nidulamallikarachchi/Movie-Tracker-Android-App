package com.example.movietracker.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.movietracker.R
import com.example.movietracker.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class EditUserProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var profileImage: ImageView
    private lateinit var editFirstName: EditText
    private lateinit var editUsername: EditText
    private lateinit var editAboutMe: EditText
    private lateinit var buttonSaveProfile: Button
    private lateinit var buttonSelectPreferences: Button
    private val userId = "user123" // Replace with actual user ID passed from the DisplayUserProfileActivity

    private lateinit var storageReference: StorageReference
    private var profilePicUrl: String = ""  // Variable to hold the uploaded image URI

    private var moviePreferences: MutableList<String> = mutableListOf()
    private val PICK_IMAGE_REQUEST = 1
    private val allMoviePreferences = arrayOf("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Romance")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)

        firestore = FirebaseFirestore.getInstance()

        editFirstName = findViewById(R.id.editFirstName)
        editUsername = findViewById(R.id.editUsername)
        editAboutMe = findViewById(R.id.editAboutMe)
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile)
        buttonSelectPreferences = findViewById(R.id.buttonSelectPreferences)

        profileImage = findViewById(R.id.imageViewProfile)
        storageReference = FirebaseStorage.getInstance().reference.child("profilePictures")

        buttonSelectPreferences.setOnClickListener {
            showMoviePreferencesDialog()
        }

        // Load the user profile if it exists
        loadUserProfile()

        // Save button click listener
        buttonSaveProfile.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun showMoviePreferencesDialog() {
        val checkedItems = BooleanArray(allMoviePreferences.size) { false }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Movie Preferences")
        builder.setMultiChoiceItems(allMoviePreferences, checkedItems) { _, which, isChecked ->
            if (isChecked) {
                moviePreferences.add(allMoviePreferences[which])
            } else {
                moviePreferences.remove(allMoviePreferences[which])
            }
        }
        builder.setPositiveButton("OK") { _, _ ->
            // You can show selected preferences or handle further logic if needed
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            uploadImageToFirebase(imageUri)
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
                    profilePicUrl = it.profilePicUrl // Retrieve the profile picture URL
                    moviePreferences = it.moviePreferences.toMutableList() // Load movie preferences
                    // Load the profile picture into ImageView using Picasso
                    Picasso.get().load(profilePicUrl).into(profileImage)
                }
            } else {
                Log.d("EditUserProfileActivity", "No such document")
            }
        }.addOnFailureListener { e ->
            Log.e("EditUserProfileActivity", "Error loading user profile: ${e.message}", e)
        }
    }

    private fun saveUserProfile() {
        val userProfile = UserProfile(
            userId = userId,
            firstName = editFirstName.text.toString(),
            username = editUsername.text.toString(),
            aboutMe = editAboutMe.text.toString(),
            profilePicUrl = profilePicUrl, // Update with the actual profile picture URL
            moviePreferences = moviePreferences // Update with the selected movie preferences
        )

        val userProfileRef = firestore.collection("userProfiles").document(userId)
        userProfileRef.set(userProfile)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                Log.d("EditUserProfileActivity", "User profile updated: ${userProfile.username}")

                // Redirect to DisplayUserProfileActivity after saving
                val intent = Intent(this, DisplayUserProfileActivity::class.java)
                intent.putExtra("USER_ID", userId) // Pass the user ID to display activity
                startActivity(intent)
                finish() // Optional: close the current activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                Log.e("EditUserProfileActivity", "Error updating profile: ${e.message}", e)
            }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        if (imageUri != null) {
            val fileReference = storageReference.child("${System.currentTimeMillis()}.jpg")
            fileReference.putFile(imageUri)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        profilePicUrl = uri.toString() // Store the uploaded image URL
                        Toast.makeText(this, "Upload successful!", Toast.LENGTH_SHORT).show()
                        Picasso.get().load(uri).into(profileImage)  // Load the image into ImageView
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }
}
