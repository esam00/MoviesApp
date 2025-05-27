package com.tasks.moviesapp.data.remote

import com.tasks.moviesapp.domain.model.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET

interface MoviesApiService {

    @GET("movie/popular")
    suspend fun getAllMovies(): Response<MovieListResponse>
}