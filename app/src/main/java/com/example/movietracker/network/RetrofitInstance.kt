package com.example.movietracker.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val api: TMDBApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBApiService::class.java)
    }

//    Retrofit.Builder() creates a Retrofit instance.
//    .baseUrl(BASE_URL) defines the base URL for all API requests.
//    .addConverterFactory(GsonConverterFactory.create()) tells Retrofit to convert JSON responses into Kotlin objects using the Gson library.
//    .create(TMDBApiService::class.java) links Retrofit with the interface (which we'll create next) that defines the API endpoints.
}