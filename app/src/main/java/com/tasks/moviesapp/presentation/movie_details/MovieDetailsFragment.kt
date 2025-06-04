package com.tasks.moviesapp.presentation.movie_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tasks.moviesapp.R
import com.tasks.moviesapp.core.Status
import com.tasks.moviesapp.core.utils.getImageUri
import com.tasks.moviesapp.databinding.FragmentMovieDetailsBinding
import com.tasks.moviesapp.core.utils.formatMinutes
import com.tasks.moviesapp.data.local.entity.MovieEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding

    private val viewModel: MovieDetailsViewModel by viewModels()

    private lateinit var movie: MovieEntity

    private val args: MovieDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailsBinding.inflate(layoutInflater, container, false)

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        getMovieById(args.movieId)
        observeFavoriteStatus()
        return binding.root
    }

    private fun getMovieById(id: Int) = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getMovieById(id).collect {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        it.data?.let { data ->
                            movie = data
                            bindMovieDetails()
                        }
                    }

                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("Movie Details", "getMovieById: ${it.message}")
                        Toast.makeText(
                            requireContext(),
                            "Failed To get updated info",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun bindMovieDetails() = with(binding) {
        tvMovieTitle.text = movie.title
        tvOverview.text = movie.overview
        tvReleaseDate.text = movie.releaseDate

        movie.runtime?.let {
            tvRunTime.text = formatMinutes(it)
        }

        Glide.with(requireContext()).load(movie.getImageUri())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(ivMoviePoster)

        ivFavorite.setImageResource(if (movie.isFavorite) R.drawable.ic_remove_favorite else R.drawable.ic_add_favorite)
        ivFavorite.setOnClickListener {
            //toggle favorite status
            viewModel.updateFavoriteStatus(movie.id, !movie.isFavorite)
        }
    }

    private fun observeFavoriteStatus() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.isFavorite.collect { isFavorite ->
                movie = movie.copy(isFavorite = isFavorite)
                binding.ivFavorite.setImageResource(if (isFavorite) R.drawable.ic_remove_favorite else R.drawable.ic_add_favorite)
            }
        }
    }
}