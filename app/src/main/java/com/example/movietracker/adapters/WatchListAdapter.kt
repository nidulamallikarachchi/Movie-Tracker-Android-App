package com.example.movietracker.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.movietracker.R
import com.example.movietracker.models.Movie

class WatchListAdapter(
    private val movies: List<Movie>,
    private val removeFromWatchlist: (Movie) -> Unit,
    private val markAsWatched: (Movie) -> Unit
) : RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>() {

    inner class WatchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster: ImageView = itemView.findViewById(R.id.moviePoster)
        val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        val movieReleaseDate: TextView = itemView.findViewById(R.id.movieReleaseDate)
        val movieRatingBar: ProgressBar = itemView.findViewById(R.id.movieRatingBar)
        val markWatchedButton: ImageButton = itemView.findViewById(R.id.markWatchedButton)
        val removeFromWatchlistButton: ImageButton = itemView.findViewById(R.id.removeFromWatchlistButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_watchlist, parent, false)
        return WatchListViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchListViewHolder, position: Int) {
        val movie = movies[position]

        // Logging the data before setting it
        Log.d("WatchListAdapter", "Movie Title: ${movie.title}")
        Log.d("WatchListAdapter", "Release Date: ${movie.release_date}")
        Log.d("WatchListAdapter", "Rating: ${movie.vote_average}")

        // Binding movie title, release date, and rating
        holder.movieTitle.text = movie.title ?: "No Title"
        holder.movieReleaseDate.text = movie.release_date ?: "Unknown"

        val ratingOutOf10 = movie.vote_average
        val progressPercentage = (ratingOutOf10 * 10).toInt()
        holder.movieRatingBar.progress = progressPercentage

        Log.d("WatchListAdapter", "Binding movie: ${movie.title}")

        // Load the movie poster using Glide (ensure the poster path is correct)
        val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path ?: ""}"
//        Glide.with(holder.itemView.context)
//            .load(posterUrl)
//            .apply(com.bumptech.glide.request.RequestOptions().transform(RoundedCorners(20)))
//            .into(holder.moviePoster)

        holder.moviePoster.load(posterUrl) {
            transformations(RoundedCornersTransformation(20f)) // Set the corner radius
        }

        // Set click listeners for buttons
        holder.markWatchedButton.setOnClickListener { markAsWatched(movie) }
        holder.removeFromWatchlistButton.setOnClickListener { removeFromWatchlist(movie) }
    }


    override fun getItemCount(): Int = movies.size
}
