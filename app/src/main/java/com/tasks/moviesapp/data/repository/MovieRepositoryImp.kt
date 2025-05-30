package com.tasks.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tasks.moviesapp.data.local.MovieDatabase
import com.tasks.moviesapp.data.paging_mediator.MovieRemoteMediator
import com.tasks.moviesapp.data.remote.MoviesApiService
import com.tasks.moviesapp.data.remote.mapper.toMovieEntity
import com.tasks.moviesapp.core.Resource
import com.tasks.moviesapp.domain.model.MovieEntity
import com.tasks.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean) {
        database.movieDao().updateFavoriteStatus(movieId, isFavorite)
    }

    override suspend fun getMovieById(movieId: Int): Flow<Resource<MovieEntity>> = flow {
        emit(Resource.loading())
        try {
            val response = apiService.getMovieById(movieId)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.success(it.toMovieEntity()))
                }
            } else {
                emit(Resource.error(response.message()))
            }

        } catch (e: Exception) {
            emit(Resource.error(e.message.toString()))
        }
    }

}