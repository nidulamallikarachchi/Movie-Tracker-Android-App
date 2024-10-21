package com.example.movietracker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movietracker.R
import com.example.movietracker.adapters.WatchListAdapter
import com.example.movietracker.models.Movie
import com.google.firebase.firestore.FirebaseFirestore

class WatchListFragment : Fragment() {

    private lateinit var watchlistRecyclerView: RecyclerView
    private lateinit var watchlistAdapter: WatchListAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val watchlist = mutableListOf<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_watchlist, container, false)

        // Initialize RecyclerView and Adapter
        watchlistRecyclerView = view.findViewById(R.id.watchlistRecyclerView)
        watchlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter with an empty list
        watchlistAdapter = WatchListAdapter(watchlist, this::removeFromWatchlist, this::markAsWatched)
        watchlistRecyclerView.adapter = watchlistAdapter

        // Load the watchlist data
        loadWatchlist()

        return view
    }

    private fun loadWatchlist() {
        val userId = "user123" // In a real app, get the logged-in user's ID
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
                    Log.d("WatchListFragment", "No movies in the watchlist.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("WatchListFragment", "Error getting documents: ", exception)
            }
    }

    private fun removeFromWatchlist(movie: Movie) {
        val userId = "user123" // In a real app, get the logged-in user's ID
        firestore.collection("watchlists").document(userId).collection("movies")
            .document(movie.id.toString())
            .delete()
            .addOnSuccessListener {
                watchlist.remove(movie)
                watchlistAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w("WatchListFragment", "Error removing document", e)
            }
    }

    private fun markAsWatched(movie: Movie) {
        val userId = "user123" // In a real app, get the logged-in user's ID
        // Add movie to the watched list
        firestore.collection("watchedMovies").document(userId).collection("movies")
            .document(movie.id.toString())
            .set(movie)
            .addOnSuccessListener {
                // Once marked as watched, remove from the watchlist
                removeFromWatchlist(movie)
            }
            .addOnFailureListener { e ->
                Log.w("WatchListFragment", "Error adding movie to watched list", e)
            }
    }
}
