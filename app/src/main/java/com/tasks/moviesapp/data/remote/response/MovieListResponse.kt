package com.tasks.moviesapp.data.remote.response

data class MovieListResponse(
    val page: Int,
    val results: List<MovieResponse>
)
