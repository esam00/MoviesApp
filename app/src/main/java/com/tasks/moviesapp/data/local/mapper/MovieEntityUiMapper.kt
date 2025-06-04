package com.tasks.moviesapp.data.local.mapper

import com.tasks.moviesapp.core.utils.getImageUri
import com.tasks.moviesapp.data.local.entity.MovieEntity
import com.tasks.moviesapp.domain.model.Movie

sealed class ViewType{
    data object Grid: ViewType()
    data object List: ViewType()
}

fun MovieEntity.toUiModel(viewType: ViewType): Movie{
    return Movie(
        id = id,
        title = title,
        releaseDate = releaseDate,
        imageUrl = getImageUri(),
        overview = overview,
        voteAverage = voteAverage,
        homepage = homepage,
        runtime = runtime,
        status = status,
        isFavorite = isFavorite,
        viewType = viewType)
}