package com.example.movietracker.network

import com.example.movietracker.models.CreditsResponse
import com.example.movietracker.models.MovieResponse
import com.example.movietracker.models.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String
    ): Response<GenreResponse>

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<CreditsResponse>

}

//    @GET("movie/popular"): This specifies that we’re making a GET request to the /movie/popular endpoint to fetch popular movies.
//    suspend fun getPopularMovies(...): Since Retrofit supports coroutines, we use suspend functions to fetch data asynchronously.
//    @Query("api_key"): We pass the TMDB API key as a query parameter.
//    @Query("page"): This is used for pagination in the popular movies API.
//    searchMovies: This endpoint allows searching for movies by title.
