package com.example.movietracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Movie class now implements Parcelable and includes the 'overview' field
@Parcelize
data class Movie(
    val id: Int = 0,
    val title: String = "",
    val poster_path: String? = null,
    val release_date: String = "",
    val vote_average: Float = 0.0f,
    val overview: String = ""   // Add the 'overview' field for movie description
) : Parcelable

//MovieResponse: The API returns a JSON object where the list of movies is stored in a results field, so this class helps map that.
data class MovieResponse (
    val results: List<Movie>
)