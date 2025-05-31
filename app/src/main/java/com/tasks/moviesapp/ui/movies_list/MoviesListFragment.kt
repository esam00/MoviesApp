package com.tasks.moviesapp.ui.movies_list

import android.os.Bundle
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
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasks.moviesapp.R
import com.tasks.moviesapp.databinding.FragmentMoviesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

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
        initPlantsAdapter()
        getMoviesList()
        setupPreviewType()
        return binding.root
    }

    private fun initPlantsAdapter() = with(binding) {
        moviesAdapter = MoviesLoadStateAdapter(
            previewType = previewType,
            onItemClicked = {
                findNavController().navigate(
                    MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(
                        it.id
                    )
                )
            },
            onFavoriteClicked = { movieId, isFavorite ->
                viewModel.toggleFavorite(movieId, isFavorite)
            })

        rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = if (previewType == PreviewType.GRID) GridLayoutManager(
                requireContext(),
                2
            ) else LinearLayoutManager(requireContext())
        }

        moviesAdapter.addLoadStateListener { loadState ->
            progressBar.isVisible =
                loadState.source.refresh is LoadState.Loading && moviesAdapter.itemCount == 0

            handleError(loadState)
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.refresh as? LoadState.Error
            ?: loadState.append as? LoadState.Error

        errorState?.let {
            Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
        }

    }

    private fun getMoviesList() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.moviesList.collectLatest {
                moviesAdapter.submitData(it)
            }
        }
    }

    private fun setupPreviewType() = with(binding) {
        icPreviewController.setOnClickListener {
            if (previewType == PreviewType.GRID) {
                previewType = PreviewType.LIST
                icPreviewController.setImageResource(R.drawable.ic_list_view)
            } else {
                previewType = PreviewType.GRID
                icPreviewController.setImageResource(R.drawable.ic_grid_view)
            }

            initPlantsAdapter()
            getMoviesList()
        }

    }
}