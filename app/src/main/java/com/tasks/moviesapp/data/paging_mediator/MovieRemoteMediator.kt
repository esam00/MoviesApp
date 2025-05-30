package com.tasks.moviesapp.data.paging_mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tasks.moviesapp.data.local.MovieDatabase
import com.tasks.moviesapp.data.local.remote_key.RemoteKey
import com.tasks.moviesapp.data.remote.MoviesApiService
import com.tasks.moviesapp.data.remote.mapper.toMovieEntity
import com.tasks.moviesapp.domain.model.MovieEntity
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val apiService: MoviesApiService,
    private val database: MovieDatabase
) : RemoteMediator<Int, MovieEntity>() {

    private val movieDao = database.movieDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            Log.d("PAGING", "load: $loadType state: $state")
            // Calculate page number based on load type
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = false)
                    // Retrieve the next page from remote key table
                    val remoteKey = database.remoteKeysDao().getRemoteKeyByMovieId(lastItem.id)
                    remoteKey?.nextPage ?: return MediatorResult.Success(true)
                }
            }

            // Fetch from network
            val response = apiService.getAllMovies(page = page)
            val movieEntities = response.body()?.results?.map { it.toMovieEntity() } ?: emptyList()

            // Calculate next page
            val nextPage = if (movieEntities.isEmpty()) null else page + 1

            val remoteKeys = movieEntities.map { movie ->
                RemoteKey(movieId = movie.id, nextPage = nextPage)
            }

            // order movies list with the original index from the api request
            val startIndex = (page - 1) * 20
            val orderedMovies = movieEntities.mapIndexed { index, movie ->
                movie.copy(originalIndex = startIndex + index)
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    val favoriteMovies = movieDao.getFavoriteMovies()

                    movieDao.clearAllMovies()
                    remoteKeysDao.clearRemoteKeys()

                    val updatedMovies = orderedMovies.map { movie ->
                        val isFavorite = favoriteMovies.any { it.id == movie.id }
                        movie.copy(isFavorite = isFavorite)
                    }

                    movieDao.insertMovies(updatedMovies)
                } else {
                    // Insert movies into database
                    movieDao.insertMovies(orderedMovies)
                }

                // Insert remote keys into database
                remoteKeysDao.insertAll(remoteKeys)

            }

            MediatorResult.Success(endOfPaginationReached = movieEntities.isEmpty())

        } catch (e: Exception) {
            MediatorResult.Error(e)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }
}