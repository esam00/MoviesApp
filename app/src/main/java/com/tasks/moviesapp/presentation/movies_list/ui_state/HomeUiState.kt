package com.tasks.moviesapp.presentation.movies_list.ui_state

import androidx.paging.PagingData
import com.tasks.moviesapp.domain.model.MovieEntity

sealed class HomeIntent {
    data class MovieClicked(val movieId: Int) : HomeIntent()
    data class FavoriteToggled(val movieId: Int, val isFavorite: Boolean) : HomeIntent()
    data object ToggleViewType : HomeIntent()
}

sealed class HomeUiEvent {
    data class NavigateToDetails(val movieId: Int) : HomeUiEvent()
    data object ToggleViewType : HomeUiEvent()
}

sealed class HomeUiStates {
    data object Loading : HomeUiStates()
    data object Empty : HomeUiStates()
    data class Success(val movies: PagingData<MovieEntity>) : HomeUiStates()
    data class Error(val message: String) : HomeUiStates()
}