package com.tasks.moviesapp.domain.repository

import androidx.paging.PagingData
import com.tasks.moviesapp.domain.model.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAllMovies(): Flow<PagingData<MovieEntity>>

    suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean)

}