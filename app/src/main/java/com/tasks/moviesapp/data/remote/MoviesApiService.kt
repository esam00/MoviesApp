package com.tasks.moviesapp.data.remote

import com.tasks.moviesapp.BuildConfig
import com.tasks.moviesapp.data.remote.response.MovieListResponse
import com.tasks.moviesapp.data.remote.response.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {
    @GET("popular")
    suspend fun getAllMovies(
        @Query(ApiConstants.PAGES_PARAM) page: Int,
        @Query(ApiConstants.API_KEY_PARAM) apiKey: String = BuildConfig.API_KEY
    ): Response<MovieListResponse>

    @GET("{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int,
        @Query(ApiConstants.API_KEY_PARAM) apiKey: String = BuildConfig.API_KEY
    ): Response<MovieResponse>
}