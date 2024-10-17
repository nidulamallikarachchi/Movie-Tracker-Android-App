package com.example.movietracker.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movietracker.R
import com.example.movietracker.models.Movie

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // Retrieve the Movie object passed via Intent
        val movie = intent.getParcelableExtra<Movie>("movie")

        if (movie != null) {
            displayMovieDetails(movie)
        }
    }

    private fun displayMovieDetails(movie: Movie) {
        // UI elements
        val movieTitle: TextView = findViewById(R.id.movieTitle)
        val moviePoster: ImageView = findViewById(R.id.moviePoster)
        val movieReleaseDate: TextView = findViewById(R.id.movieReleaseDate)
        val movieRating: TextView = findViewById(R.id.movieRating)
        val movieOverview: TextView = findViewById(R.id.movieOverview)

        // Set movie details
        movieTitle.text = movie.title
        movieReleaseDate.text = "Release Date: ${movie.release_date}"
        movieRating.text = "Rating: ${movie.vote_average}/10"
        movieOverview.text = movie.overview

        // Load the movie poster using Glide
        val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
        Glide.with(this)
            .load(posterUrl)
            .into(moviePoster)
    }
}
