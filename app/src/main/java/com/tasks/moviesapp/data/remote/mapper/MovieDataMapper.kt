package com.tasks.moviesapp.data.remote.mapper

import com.tasks.moviesapp.data.remote.response.MovieResponse
import com.tasks.moviesapp.domain.model.MovieEntity

fun MovieResponse.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        releaseDate = releaseDate,
        posterPath = posterPath,
        overview = overview,
        homepage = homepage,
        runtime = runtime,
        status = status,
        voteAverage = voteAverage,
    )
}