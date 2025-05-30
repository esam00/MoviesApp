package com.tasks.moviesapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
    val overview: String,
    val voteAverage: String,
    val homepage: String?,
    val runtime: Int?,
    val status: String?,
    val originalIndex: Int = 0,
    val isFavorite: Boolean = false
)


