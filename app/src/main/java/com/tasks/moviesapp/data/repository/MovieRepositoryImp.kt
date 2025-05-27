package com.tasks.moviesapp.data.repository

import com.tasks.moviesapp.core.Resource
import com.tasks.moviesapp.data.remote.MoviesApiService
import com.tasks.moviesapp.domain.model.MovieEntity
import com.tasks.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(
    private val apiService: MoviesApiService
) : MovieRepository {
    override fun getAllMovies(): Flow<Resource<List<MovieEntity>>> = flow {
        emit(Resource.loading(null))
        try {
            val response = apiService.getAllMovies()
            if (response.isSuccessful && response.code() == 200) {
                response.body()?.result?.let { list ->
                    if (list.isNotEmpty()) {
                        emit(Resource.success(data = list))
                    } else {
                        emit(Resource.empty())
                    }
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }

}