package com.tasks.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tasks.moviesapp.data.local.MovieDatabase
import com.tasks.moviesapp.data.paging_mediator.MovieRemoteMediator
import com.tasks.moviesapp.data.remote.MoviesApiService
import com.tasks.moviesapp.domain.model.MovieEntity
import com.tasks.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImp @Inject constructor(
    private val apiService: MoviesApiService,
    private val database: MovieDatabase
) : MovieRepository {
    override fun getAllMovies(): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 40,
                prefetchDistance = 10,
                enablePlaceholders = true
            ),
            remoteMediator = MovieRemoteMediator(apiService = apiService, database = database),
            pagingSourceFactory = { database.movieDao().pagingSource() }
        ).flow
    }
}