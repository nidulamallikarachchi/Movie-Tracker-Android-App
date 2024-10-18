package com.example.movietracker.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movietracker.R
import com.example.movietracker.adapters.WatchListAdapter
import com.example.movietracker.models.Movie
import com.google.firebase.firestore.FirebaseFirestore

class WatchListActivity : AppCompatActivity() {

    private lateinit var watchlistRecyclerView: RecyclerView
    private lateinit var watchlistAdapter: WatchListAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val watchlist = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchlist)

        // Initialize RecyclerView and Adapter
        watchlistRecyclerView = findViewById(R.id.watchlistRecyclerView)
        watchlistRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with an empty list
        watchlistAdapter = WatchListAdapter(watchlist, this::removeFromWatchlist, this::markAsWatched)
        watchlistRecyclerView.adapter = watchlistAdapter

        // Load the watchlist data
        loadWatchlist()


    }


    private fun loadWatchlist() {
        val userId = "user123"
        firestore.collection("watchlists").document(userId).collection("movies")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val movie = document.toObject(Movie::class.java)
                        watchlist.add(movie)
                    }
                    // Notify the adapter that the data has been updated
                    watchlistAdapter.notifyDataSetChanged()
                } else {
                    Log.d("WatchListActivity", "No movies in the watchlist.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("WatchListActivity", "Error getting documents: ", exception)
            }
    }



    private fun removeFromWatchlist(movie: Movie) {
        val userId = "user123"  // In a real app, get the logged-in user's ID
        firestore.collection("watchlists").document(userId).collection("movies").document(movie.id.toString())
            .delete()
            .addOnSuccessListener {
                watchlist.remove(movie)
                watchlistAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w("WatchListActivity", "Error removing document", e)
            }
    }

    private fun markAsWatched(movie: Movie) {
        val userId = "user123"  // In a real app, get the logged-in user's ID
        // Add movie to the watched list
        firestore.collection("watchedMovies").document(userId).collection("movies").document(movie.id.toString())
            .set(movie)
            .addOnSuccessListener {
                // Once marked as watched, remove from the watchlist
                removeFromWatchlist(movie)
            }
            .addOnFailureListener { e ->
                Log.w("WatchListActivity", "Error adding movie to watched list", e)
            }
    }
}
