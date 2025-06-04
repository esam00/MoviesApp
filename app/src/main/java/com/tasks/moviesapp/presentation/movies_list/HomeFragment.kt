package com.tasks.moviesapp.presentation.movies_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasks.moviesapp.R
import com.tasks.moviesapp.data.local.mapper.ViewType
import com.tasks.moviesapp.databinding.FragmentMoviesListBinding
import com.tasks.moviesapp.presentation.movies_list.ui_state.HomeIntent
import com.tasks.moviesapp.presentation.movies_list.ui_state.HomeUiEvent
import com.tasks.moviesapp.presentation.movies_list.ui_state.HomeUiStates
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var moviesAdapter: MoviesLoadStateAdapter

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

        setupLoadStateAdapter()
        setupLoadStateListener()
        observePagingData()
        observeUiStates()
        observeUiEvents()
        setupPreviewType()
    }

    private fun setupLoadStateAdapter() = with(binding) {
        moviesAdapter = MoviesLoadStateAdapter(
            onItemClicked = { viewModel.processIntent(HomeIntent.MovieClicked(it)) },
            onFavoriteClicked = { movieId, isFavorite ->
                viewModel.processIntent(HomeIntent.FavoriteToggled(movieId, isFavorite))
            })

        rvMovies.adapter = moviesAdapter
        rvMovies.layoutManager = if (viewModel.viewType == ViewType.Grid)
        GridLayoutManager(requireContext(), 2)
        else LinearLayoutManager(requireContext())
    }

    private fun setupLoadStateListener() {
        moviesAdapter.addLoadStateListener { loadState ->
            viewModel.handleLoadStates(loadState, moviesAdapter.itemCount)
        }
    }

    private fun observePagingData() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.pagingDataFlow.collectLatest {
            Log.d("HomeFragment", "new page collected: $it")
            moviesAdapter.submitData(it)
        }
    }

    private fun observeUiStates() = viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collectLatest { uiStates ->
                Log.d("HomeFragment", "ui state changed : $uiStates")
                when (uiStates) {
                    is HomeUiStates.LoadingRefresh -> {
                        binding.progressBar.isVisible = true
                        binding.rvMovies.isVisible = false
                    }

                    is HomeUiStates.Empty -> {
                        binding.progressBar.isVisible = false
                    }

                    is HomeUiStates.Success -> {
                        binding.progressBar.isVisible = false
                        binding.rvMovies.isVisible = true
                    }

                    is HomeUiStates.Error -> {
                        binding.progressBar.isVisible = false
                        Log.e("Paging", "observeUiStates: ${uiStates.message}")
                        Toast.makeText(
                            requireContext(),
                            "Failed To get updated movies!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun observeUiEvents() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is HomeUiEvent.NavigateToDetails -> {
                        val action =
                            HomeFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(
                                event.movieId
                            )
                        findNavController().navigate(action)
                    }

                    HomeUiEvent.ToggleViewType -> {
                        togglePreviewIcon()
                        moviesAdapter.refresh()
                        setupLoadStateAdapter()
                    }
                }
            }
        }
    }

    private fun setupPreviewType() = with(binding) {
        icPreviewController.setOnClickListener {
            viewModel.processIntent(HomeIntent.ToggleViewType)
        }
    }

    private fun togglePreviewIcon() = with(binding) {
        icPreviewController.setImageResource(
            when (viewModel.viewType) {
                ViewType.List -> R.drawable.ic_grid_view
                ViewType.Grid -> R.drawable.ic_list_view
            }
        )
    }

}