package com.tasks.moviesapp.ui.movie_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tasks.moviesapp.R
import com.tasks.moviesapp.core.Status
import com.tasks.moviesapp.core.getImageUri
import com.tasks.moviesapp.databinding.FragmentMovieDetailsBinding
import com.tasks.moviesapp.domain.formatMinutes
import com.tasks.moviesapp.domain.model.MovieEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val args: MovieDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailsBinding.inflate(layoutInflater, container, false)
        getMovieById(args.movieId)
        return binding.root
    }

    private fun getMovieById(id: Int) = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getMovieById(id).collect {
                when (it.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        it.data?.let { movie ->
                            bindMovieDetails(movie)
                        }
                    }

                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun bindMovieDetails(movie: MovieEntity) = with(binding) {
        tvMovieTitle.text = movie.title
        tvOverview.text = movie.overview
        tvReleaseDate.text = movie.releaseDate
        movie.runtime?.let {
            tvRunTime.text = formatMinutes(it)
        }
        ivFavorite.setImageResource(if (movie.isFavorite) R.drawable.ic_remove_favorite else R.drawable.ic_add_favorite)
        Glide.with(requireContext()).load(movie.getImageUri())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(ivMoviePoster)
    }
}