package com.example.movietracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.movietracker.models.Movie
import com.example.movietracker.R
import com.google.ai.client.generativeai.common.RequestOptions

class MovieAdapter(
    private val movies: List<Movie>,
    private val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster: ImageView = itemView.findViewById(R.id.moviePoster)
        val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        val movieReleaseDate: TextView = itemView.findViewById(R.id.movieReleaseDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.movieTitle.text = movie.title
        holder.movieReleaseDate.text = movie.release_date

        // Load the movie poster using Glide
        val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
//        Glide.with(holder.itemView.context)
//            .load(posterUrl)
//            .apply(com.bumptech.glide.request.RequestOptions().transform(RoundedCorners(20)))
//            .into(holder.moviePoster)

        holder.moviePoster.load(posterUrl) {
            transformations(RoundedCornersTransformation(20f)) // Set the corner radius
        }

        // Handle item click
        holder.itemView.setOnClickListener { onClick(movie) }
    }

    override fun getItemCount(): Int = movies.size

//    MovieAdapter: This class extends RecyclerView.Adapter and binds the movie data to each grid item in the RecyclerView.
//    Glide: We use the Glide library to load images (movie posters) efficiently from the URL provided by TMDB.
}
