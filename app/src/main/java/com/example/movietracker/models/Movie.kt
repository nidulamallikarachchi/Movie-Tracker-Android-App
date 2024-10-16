package com.example.movietracker.models

//Movie: This data class represents a movie, including properties like id, title, poster_path, release_date, etc.
data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val release_date: String,
    val vote_average: Float,
    val overview: String
)

//MovieResponse: The API returns a JSON object where the list of movies is stored in a results field, so this class helps map that.
data class MovieResponse (
    val results: List<Movie>
)