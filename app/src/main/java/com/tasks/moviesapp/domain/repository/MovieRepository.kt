package com.tasks.moviesapp.domain.repository

import com.tasks.moviesapp.core.Resource
import com.tasks.moviesapp.domain.model.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAllMovies(): Flow<Resource<List<MovieEntity>>>
}