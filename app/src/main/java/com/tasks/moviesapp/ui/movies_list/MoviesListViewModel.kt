package com.tasks.moviesapp.ui.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tasks.moviesapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val moviesList = movieRepository.getAllMovies().cachedIn(viewModelScope)

    fun toggleFavorite(movieId: Int, isFavorite: Boolean) = viewModelScope.launch {
        movieRepository.updateFavoriteStatus(movieId, isFavorite)
    }
}