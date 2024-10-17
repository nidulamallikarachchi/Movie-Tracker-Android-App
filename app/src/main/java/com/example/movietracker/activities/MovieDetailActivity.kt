package com.example.movietracker.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movietracker.R
import com.example.movietracker.models.Movie
import com.google.firebase.firestore.FirebaseFirestore

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var addToWatchlistButton: Button
    private lateinit var addToWatchedButton: Button
    private val userId = "user123" // Replace with actual user ID after implementing authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        firestore = FirebaseFirestore.getInstance() // Initialize Firestore

        val movie = intent.getParcelableExtra<Movie>("movie")

        if (movie != null) {
            displayMovieDetails(movie)
            setupButtons(movie)
        }
    }

    private fun displayMovieDetails(movie: Movie) {
        val movieTitle: TextView = findViewById(R.id.movieTitle)
        val moviePoster: ImageView = findViewById(R.id.moviePoster)
        val movieReleaseDate: TextView = findViewById(R.id.movieReleaseDate)
        val movieRating: TextView = findViewById(R.id.movieRating)
        val movieOverview: TextView = findViewById(R.id.movieOverview)

        movieTitle.text = movie.title
        movieReleaseDate.text = "Release Date: ${movie.release_date}"
        movieRating.text = "Rating: ${movie.vote_average}/10"
        movieOverview.text = movie.overview

        val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
        Glide.with(this)
            .load(posterUrl)
            .into(moviePoster)
    }

    private fun setupButtons(movie: Movie) {
        addToWatchlistButton = findViewById(R.id.addToWatchlistButton)
        addToWatchedButton = findViewById(R.id.addToWatchedButton)

        addToWatchlistButton.setOnClickListener {
            checkAndAddToWatchlist(movie)
        }

        addToWatchedButton.setOnClickListener {
            checkAndAddToWatched(movie)
        }
    }

    private fun checkAndAddToWatchlist(movie: Movie) {
        val watchlistRef = firestore.collection("watchlists").document(userId)
            .collection("movies").document(movie.id.toString())

        val watchedMoviesRef = firestore.collection("watchedMovies").document(userId)
            .collection("movies").document(movie.id.toString())

        watchedMoviesRef.get().addOnSuccessListener { watchedDoc ->
            if (watchedDoc.exists()) {
                Toast.makeText(this, "You have already watched this movie", Toast.LENGTH_SHORT).show()
            } else {
                watchlistRef.get().addOnSuccessListener { watchlistDoc ->
                    if (watchlistDoc.exists()) {
                        Toast.makeText(this, "This movie is already in your watchlist", Toast.LENGTH_SHORT).show()
                    } else {
                        val movieData = hashMapOf(
                            "id" to movie.id,
                            "title" to movie.title,
                            "poster_path" to movie.poster_path,
                            "release_date" to movie.release_date,
                            "vote_average" to movie.vote_average
                        )
                        watchlistRef.set(movieData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Movie added to watchlist", Toast.LENGTH_SHORT).show()
                                Log.d("MovieDetail", "Movie added to watchlist: ${movie.title}")
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to add movie to watchlist", Toast.LENGTH_SHORT).show()
                                Log.e("MovieDetail", "Error adding movie to watchlist: ${e.message}", e)
                            }
                    }
                }.addOnFailureListener { e ->
                    Log.e("MovieDetail", "Error fetching watchlist document: ${e.message}", e)
                }
            }
        }.addOnFailureListener { e ->
            Log.e("MovieDetail", "Error fetching watched movie document: ${e.message}", e)
        }
    }

    private fun checkAndAddToWatched(movie: Movie) {
        val watchlistRef = firestore.collection("watchlists").document(userId)
            .collection("movies").document(movie.id.toString())

        val watchedMoviesRef = firestore.collection("watchedMovies").document(userId)
            .collection("movies").document(movie.id.toString())

        watchedMoviesRef.get().addOnSuccessListener { watchedDoc ->
            if (watchedDoc.exists()) {
                Toast.makeText(this, "This movie is already in your watched list", Toast.LENGTH_SHORT).show()
            } else {
                val movieData = hashMapOf(
                    "id" to movie.id,
                    "title" to movie.title,
                    "poster_path" to movie.poster_path,
                    "release_date" to movie.release_date,
                    "vote_average" to movie.vote_average
                )
                watchedMoviesRef.set(movieData)
                    .addOnSuccessListener {
                        watchlistRef.delete().addOnSuccessListener {
                            Toast.makeText(this, "Movie added to watched movies", Toast.LENGTH_SHORT).show()
                            Log.d("MovieDetail", "Movie added to watched movies: ${movie.title}")
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to add movie to watched list", Toast.LENGTH_SHORT).show()
                        Log.e("MovieDetail", "Error adding movie to watched list: ${e.message}", e)
                    }
            }
        }.addOnFailureListener { e ->
            Log.e("MovieDetail", "Error fetching watched movie document: ${e.message}", e)
        }
    }
}
