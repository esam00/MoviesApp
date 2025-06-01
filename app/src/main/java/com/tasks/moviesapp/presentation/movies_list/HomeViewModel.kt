package com.tasks.moviesapp.presentation.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.tasks.moviesapp.domain.repository.MovieRepository
import com.tasks.moviesapp.presentation.movies_list.ui_state.HomeIntent
import com.tasks.moviesapp.presentation.movies_list.ui_state.HomeUiEvent
import com.tasks.moviesapp.presentation.movies_list.ui_state.HomeUiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiEvent = Channel<HomeUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow<HomeUiStates>(HomeUiStates.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadMovies() = movieRepository.getAllMovies().cachedIn(viewModelScope)

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.MovieClicked -> {
                viewModelScope.launch {
                    _uiEvent.send(HomeUiEvent.NavigateToDetails(intent.movieId))
                }
            }

            is HomeIntent.FavoriteToggled -> {
                viewModelScope.launch {
                    movieRepository.updateFavoriteStatus(intent.movieId, intent.isFavorite)
                }
            }

            HomeIntent.ToggleViewType -> {
                viewModelScope.launch {
                    _uiEvent.send(HomeUiEvent.ToggleViewType)
                }
            }
        }
    }

    fun handleLoadStates(loadState: CombinedLoadStates, itemCount: Int) {
        when {
            loadState.source.refresh is LoadState.Loading && itemCount == 0 -> {
                _uiState.value = HomeUiStates.Loading
            }

            loadState.source.refresh is LoadState.NotLoading && itemCount > 0 -> {
                _uiState.value = HomeUiStates.Success
            }

            loadState.source.refresh is LoadState.NotLoading && itemCount == 0 -> {
                _uiState.value = HomeUiStates.Empty
            }

            loadState.source.refresh is LoadState.Error -> {
                val error = (loadState.source.refresh as LoadState.Error).error
                _uiState.value = HomeUiStates.Error(error.message ?: "Failed to Update!")
            }
        }
    }
}