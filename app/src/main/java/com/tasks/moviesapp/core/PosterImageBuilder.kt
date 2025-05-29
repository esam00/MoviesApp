package com.tasks.moviesapp.core

import android.net.Uri
import com.tasks.moviesapp.domain.model.MovieEntity
/**
 * Builds the full image URL for the movie poster using TMDB's image API.
 * Reference: https://developer.themoviedb.org/docs/image-basics
 * Uses the "w500" image size
 */

fun MovieEntity.getImageUri(): String {
    val builder = Uri.Builder()
    builder.scheme("https")
        .authority("image.tmdb.org")
        .appendPath("t")
        .appendPath("p")
        .appendPath("w500")
        .appendEncodedPath(posterPath)

    val myUrl = builder.build().toString()
    return myUrl
}