package com.example.movietracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movietracker.R
import com.example.movietracker.adapters.WatchedMoviesAdapter
import com.example.movietracker.models.Movie
import com.google.firebase.firestore.FirebaseFirestore

class WatchedListFragment : Fragment() {

    private lateinit var watchedMoviesRecyclerView: RecyclerView
    private lateinit var watchedMoviesAdapter: WatchedMoviesAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val watchedMovies = mutableListOf<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_watched_movies, container, false)

        watchedMoviesRecyclerView = view.findViewById(R.id.watchedMoviesRecyclerView)
        watchedMoviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadWatchedMovies()

        return view
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
                Log.e("WatchedMoviesFragment", "Error getting watched movies: ", exception)
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
                Log.w("WatchedMoviesFragment", "Error removing movie from watched list", e)
            }
    }
}
