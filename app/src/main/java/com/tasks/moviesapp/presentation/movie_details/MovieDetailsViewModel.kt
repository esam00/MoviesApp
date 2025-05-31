package com.tasks.moviesapp.presentation.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasks.moviesapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _isFavorite = MutableSharedFlow<Boolean>()
    val isFavorite = _isFavorite.asSharedFlow()

    suspend fun getMovieById(movieId: Int) = movieRepository.getMovieById(movieId)

    fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean) = viewModelScope.launch {
        movieRepository.updateFavoriteStatus(movieId, isFavorite)
        _isFavorite.emit(isFavorite)
    }

}