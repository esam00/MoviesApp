package com.tasks.moviesapp.domain.model

data class MovieEntity(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
    val overview: String,
    val voteAverage: String,
    val homepage: String?,
    val runtime: Int?,
    val status: String?,
    val genres: List<GenreEntity>?
)


