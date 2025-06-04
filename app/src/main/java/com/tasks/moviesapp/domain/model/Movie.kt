package com.tasks.moviesapp.domain.model

import com.tasks.moviesapp.data.local.mapper.ViewType

data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val imageUrl: String,
    val overview: String,
    val voteAverage: String,
    val homepage: String?,
    val runtime: Int?,
    val status: String?,
    val isFavorite: Boolean = false,
    val viewType : ViewType = ViewType.Grid
)
