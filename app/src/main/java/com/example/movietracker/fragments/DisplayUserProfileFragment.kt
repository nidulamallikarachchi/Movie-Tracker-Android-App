package com.example.movietracker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.movietracker.R
import com.example.movietracker.activities.EditUserProfileActivity
import com.example.movietracker.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class DisplayUserProfileFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var profileImage: ImageView
    private lateinit var textFirstName: TextView
    private lateinit var textUsername: TextView
    private lateinit var textAboutMe: TextView
    private lateinit var textMoviePreferences: TextView
    private val userId = "user123" // Replace with actual user ID passed from the previous fragment/activity

    private lateinit var buttonEditProfile: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_display_user_profile, container, false)

        // Initialize Firestore and views
        firestore = FirebaseFirestore.getInstance()
        profileImage = view.findViewById(R.id.imageViewProfile)
        textFirstName = view.findViewById(R.id.textFirstName)
        textUsername = view.findViewById(R.id.textUsername)
        textAboutMe = view.findViewById(R.id.textAboutMe)
        textMoviePreferences = view.findViewById(R.id.textMoviePreferences)
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile)

        // Load the user profile
        loadUserProfile(userId)

        buttonEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditUserProfileActivity::class.java)
            intent.putExtra("USER_ID", userId) // Pass user ID to edit activity
            startActivity(intent)
        }

        return view
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
                Log.d("DisplayUserProfileFragment", "No such document")
            }
        }.addOnFailureListener { e ->
            Log.e("DisplayUserProfileFragment", "Error loading user profile: ${e.message}", e)
        }
    }
}
