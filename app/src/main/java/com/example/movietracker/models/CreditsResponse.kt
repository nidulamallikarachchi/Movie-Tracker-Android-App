package com.example.movietracker.models

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("cast")
    val cast: List<Cast>
)

data class Cast(
    @SerializedName("name")
    val name: String,

    @SerializedName("profile_path")
    val profilePath: String? // Nullable in case no profile image is available
)
