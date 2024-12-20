package com.example.movietracker.adapters

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

class WatchedMoviesAdapter(
    private val movies: List<Movie>,
    private val removeFromWatchedMovies: (Movie) -> Unit
) : RecyclerView.Adapter<WatchedMoviesAdapter.WatchedMoviesViewHolder>() {

    inner class WatchedMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster: ImageView = itemView.findViewById(R.id.moviePoster)
        val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        val movieReleaseDate: TextView = itemView.findViewById(R.id.movieReleaseDate)
        val movieRatingBar: ProgressBar = itemView.findViewById(R.id.movieRatingBar)
        val removeFromWatchedButton: ImageButton = itemView.findViewById(R.id.removeFromWatchedButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchedMoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_watched, parent, false)
        return WatchedMoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchedMoviesViewHolder, position: Int) {
        val movie = movies[position]
        holder.movieTitle.text = movie.title
        holder.movieReleaseDate.text = movie.release_date

        val ratingOutOf10 = movie.vote_average
        val progressPercentage = (ratingOutOf10 * 10).toInt()
        holder.movieRatingBar.progress = progressPercentage

        val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
//        Glide.with(holder.itemView.context)
//            .load(posterUrl)
//            .apply(com.bumptech.glide.request.RequestOptions().transform(RoundedCorners(20)))
//            .into(holder.moviePoster)

        holder.moviePoster.load(posterUrl) {
            transformations(RoundedCornersTransformation(20f)) // Set the corner radius
        }

        holder.removeFromWatchedButton.setOnClickListener {
            removeFromWatchedMovies(movie)
        }
    }

    override fun getItemCount(): Int = movies.size
}
