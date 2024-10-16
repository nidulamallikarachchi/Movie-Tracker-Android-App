package com.example.movietracker.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movietracker.R
import com.example.movietracker.adapters.WatchedMoviesAdapter
import com.example.movietracker.models.Movie
import com.google.firebase.firestore.FirebaseFirestore

class WatchedMoviesActivity : AppCompatActivity() {

    private lateinit var watchedMoviesRecyclerView: RecyclerView
    private lateinit var watchedMoviesAdapter: WatchedMoviesAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val watchedMovies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watched_movies)

        watchedMoviesRecyclerView = findViewById(R.id.watchedMoviesRecyclerView)
        watchedMoviesRecyclerView.layoutManager = LinearLayoutManager(this)

        loadWatchedMovies()
    }

    private fun loadWatchedMovies() {
        val userId = "user123"  // Replace with the actual logged-in user ID
        firestore.collection("watchedMovies").document(userId).collection("movies")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val movie = document.toObject(Movie::class.java)
                    watchedMovies.add(movie)
                }
                watchedMoviesAdapter = WatchedMoviesAdapter(watchedMovies, this::removeFromWatchedMovies)
                watchedMoviesRecyclerView.adapter = watchedMoviesAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("WatchedMoviesActivity", "Error getting watched movies: ", exception)
            }
    }

    private fun removeFromWatchedMovies(movie: Movie) {
        val userId = "user123"  // Replace with the actual logged-in user ID
        firestore.collection("watchedMovies").document(userId).collection("movies").document(movie.id.toString())
            .delete()
            .addOnSuccessListener {
                watchedMovies.remove(movie)
                watchedMoviesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w("WatchedMoviesActivity", "Error removing movie from watched list", e)
            }
    }
}
