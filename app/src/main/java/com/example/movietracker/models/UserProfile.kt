package com.example.movietracker.models

data class UserProfile(
    val userId: String = "",             // Default value for userId
    val firstName: String = "",          // Default value for firstName
    val username: String = "",            // Default value for username
    val aboutMe: String = "",             // Default value for aboutMe
    val profilePicUrl: String = "",       // Default value for profilePicId
    val moviePreferences: List<String> = listOf() // Default value for moviePreferences
) {
    // No-argument constructor required for Firestore
    constructor() : this("", "", "", "", "", listOf())
}
