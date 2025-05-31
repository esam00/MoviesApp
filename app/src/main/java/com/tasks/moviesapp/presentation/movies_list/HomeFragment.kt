package com.tasks.moviesapp.presentation.movies_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasks.moviesapp.R
import com.tasks.moviesapp.databinding.FragmentMoviesListBinding
import com.tasks.moviesapp.presentation.movies_list.ui_state.HomeIntent
import com.tasks.moviesapp.presentation.movies_list.ui_state.HomeUiEvent
import com.tasks.moviesapp.presentation.movies_list.ui_state.HomeUiStates
import com.tasks.moviesapp.presentation.movies_list.ui_state.PreviewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding

    private val viewModel: MoviesListViewModel by viewModels()

    private lateinit var moviesAdapter: MoviesLoadStateAdapter

    private var previewType = PreviewType.GRID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoviesListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPreviewType()
        initMoviesAdapter()
        observeUiStates()
        observeUiEvents()
    }

    private fun setupPreviewType() = with(binding) {
        icPreviewController.setOnClickListener {
            viewModel.processIntent(HomeIntent.ToggleViewType)
        }
    }

    private fun initMoviesAdapter() = with(binding) {
        moviesAdapter = MoviesLoadStateAdapter(
            previewType = previewType,
            onItemClicked = { viewModel.processIntent(HomeIntent.MovieClicked(it)) },
            onFavoriteClicked = { movieId, isFavorite ->
                viewModel.processIntent(HomeIntent.FavoriteToggled(movieId, isFavorite))
            })

        rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = if (previewType == PreviewType.GRID) GridLayoutManager(
                requireContext(),
                2
            ) else LinearLayoutManager(requireContext())
        }

        moviesAdapter.addLoadStateListener { loadState ->
            viewModel.handleLoadStates(loadState, moviesAdapter.itemCount)
        }
    }

    private fun observeUiStates() = lifecycleScope.launch {
        viewModel.uiState.collectLatest { uiStates ->
            when (uiStates) {
                is HomeUiStates.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is HomeUiStates.Empty -> {
                    binding.progressBar.isVisible = false

                }

                is HomeUiStates.Success -> {
                    binding.progressBar.isVisible = false
                    moviesAdapter.submitData(uiStates.movies)

                }

                is HomeUiStates.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), uiStates.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun observeUiEvents() = lifecycleScope.launch {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToDetails -> {
                    val action =
                        HomeFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(event.movieId)
                    findNavController().navigate(action)
                }

                HomeUiEvent.ToggleViewType -> {
                    togglePreviewType()
                }
            }
        }
    }

    private fun togglePreviewType() = with(binding) {
        previewType = when (previewType) {
            PreviewType.LIST -> PreviewType.GRID
            PreviewType.GRID -> PreviewType.LIST
        }

        icPreviewController.setImageResource(
            when (previewType) {
                PreviewType.LIST -> R.drawable.ic_grid_view
                PreviewType.GRID -> R.drawable.ic_list_view
            }
        )

        rvMovies.layoutManager = if (previewType == PreviewType.GRID) GridLayoutManager(
            requireContext(),
            2
        ) else LinearLayoutManager(requireContext())

        moviesAdapter.notifyPreviewTypeChange(previewType)
    }
}