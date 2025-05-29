package com.tasks.moviesapp.domain.model

data class MovieListResponse(
    val page: Int,
    val results: List<MovieEntity>
)
