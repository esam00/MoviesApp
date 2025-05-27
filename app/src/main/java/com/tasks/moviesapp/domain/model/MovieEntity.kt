package com.tasks.moviesapp.domain.model

import com.google.gson.annotations.SerializedName

data class MovieEntity(
    val id: Int,
    val title: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("status") val status: String,
    @SerializedName("vote_average") val voteAverage: String,
    @SerializedName("genres") val genres: List<GenreEntity>
)


