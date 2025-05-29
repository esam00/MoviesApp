package com.tasks.moviesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tasks.moviesapp.data.paging_source.MoviesPagingSource
import com.tasks.moviesapp.data.remote.MoviesApiService
import com.tasks.moviesapp.domain.model.MovieEntity
import com.tasks.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 30

class MovieRepositoryImp @Inject constructor(
    private val apiService: MoviesApiService
) : MovieRepository {
    override fun getAllMovies(): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(apiService) }
        ).flow
    }
}