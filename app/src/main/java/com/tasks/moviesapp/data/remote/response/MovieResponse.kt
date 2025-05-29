package com.tasks.moviesapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val id: Int,
    val title: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val voteAverage: String,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("genres") val genres: List<GenreResponse>?
)


