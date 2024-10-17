package com.example.movietracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movietracker.activities.MovieDetailActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        movieRecyclerView = findViewById(R.id.movieRecyclerView)
        movieRecyclerView.layoutManager = GridLayoutManager(this, 2)

        fetchPopularMovies()

        val watchlistButton: Button = findViewById(R.id.watchlistButton)
        watchlistButton.setOnClickListener {
            val intent = Intent(this, WatchListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchPopularMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getPopularMovies("6b198ede282a11fc260cf5e13162cb0b")
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

//    Explanation:
//
//    We make the network call inside a coroutine (Dispatchers.IO) to ensure the network operation happens on a background thread.
//    After the data is fetched, we switch back to the main thread (Dispatchers.Main) to update the UI.
//    The MovieAdapter is set on the RecyclerView to display the fetched movies.
//    Note: Replace "YOUR_API_KEY" with your actual TMDB API key.
}
