package com.tasks.moviesapp.presentation.movie_details

import androidx.lifecycle.ViewModel
import com.tasks.moviesapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    suspend fun getMovieById(movieId: Int) = movieRepository.getMovieById(movieId)

}