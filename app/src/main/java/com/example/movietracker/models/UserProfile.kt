package com.example.movietracker.models

data class UserProfile(
    val userId: String = "",
    val firstName: String = "",
    val username: String = "",
    val aboutMe: String = "",
    val profilePicId: String = "", // You can use an URL or ID for the profile picture
    val moviePreferences: List<String> = emptyList() // List of favorite genres or movie types
)
