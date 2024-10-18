package com.example.movietracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movietracker.activities.DisplayUserProfileActivity
import com.example.movietracker.activities.MovieDetailActivity
import com.example.movietracker.activities.EditUserProfileActivity
import com.example.movietracker.activities.WatchListActivity
import com.example.movietracker.adapters.MovieAdapter
import com.example.movietracker.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var genreSpinner: Spinner
    private val apiKey = "6b198ede282a11fc260cf5e13162cb0b" // Replace with your actual API key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        movieRecyclerView = findViewById(R.id.movieRecyclerView)
        movieRecyclerView.layoutManager = GridLayoutManager(this, 2)
        genreSpinner = findViewById(R.id.genreSpinner)

        fetchGenres()

        val watchlistButton: Button = findViewById(R.id.watchlistButton)
        watchlistButton.setOnClickListener {
            val intent = Intent(this, WatchListActivity::class.java)
            startActivity(intent)
        }

        val userProfileButton: ImageButton = findViewById(R.id.buttonUserProfile)
        userProfileButton.setOnClickListener {
            val intent = Intent(this, DisplayUserProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchGenres() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getGenres(apiKey)
                if (response.isSuccessful) {
                    val genres = response.body()?.genres ?: emptyList()
                    withContext(Dispatchers.Main) {
                        val genreNames = genres.map { it.name }
                        val adapter = ArrayAdapter(this@HomeActivity, android.R.layout.simple_spinner_item, genreNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        genreSpinner.adapter = adapter

                        // Set an item selected listener to fetch movies by genre
                        genreSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                val selectedGenre = genres[position]
                                fetchMoviesByGenre(selectedGenre.id)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // No action needed
                            }
                        })
                    }
                } else {
                    Log.e("HomeActivity", "Failed to fetch genres: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeActivity", "Exception: ${e.message}")
            }
        }
    }

    private fun fetchMoviesByGenre(genreId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getMoviesByGenre(apiKey, genreId)
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    withContext(Dispatchers.Main) {
                        movieAdapter = MovieAdapter(movies) { movie ->
                            val intent = Intent(this@HomeActivity, MovieDetailActivity::class.java)
                            intent.putExtra("movie", movie)  // Pass the Movie object
                            startActivity(intent)
                        }
                        movieRecyclerView.adapter = movieAdapter
                    }
                } else {
                    Log.e("HomeActivity", "Failed to fetch movies: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeActivity", "Exception: ${e.message}")
            }
        }
    }
}

