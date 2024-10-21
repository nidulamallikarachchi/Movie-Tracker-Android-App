package com.example.movietracker.models

data class UserProfile(
    val userId: String = "",
    val firstName: String = "",
    val username: String = "",
    val aboutMe: String = "",
    val profilePicUrl: String = "",
    val moviePreferences: List<String> = listOf()
) {
    // No-argument constructor required for Firestore
    constructor() : this("", "", "", "", "", listOf())
}
