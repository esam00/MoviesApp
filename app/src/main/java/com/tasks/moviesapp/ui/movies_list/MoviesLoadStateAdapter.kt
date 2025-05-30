package com.tasks.moviesapp.ui.movies_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tasks.moviesapp.R
import com.tasks.moviesapp.core.getImageUri
import com.tasks.moviesapp.databinding.ItemMovieBinding
import com.tasks.moviesapp.domain.model.MovieEntity

class MoviesLoadStateAdapter(
    private val onItemClicked: (MovieEntity) -> Unit,
    private val onFavoriteClicked: (Int, Boolean) -> Unit
) : PagingDataAdapter<MovieEntity, MoviesLoadStateAdapter.ViewHolder>(REPO_COMPARATOR) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMovieBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                getItem(bindingAdapterPosition)?.let {
                    onItemClicked(it)
                }
            }

            binding.ivFavorite.setOnClickListener {
                getItem(bindingAdapterPosition)?.let {
                    onFavoriteClicked(it.id, !it.isFavorite)
                }
            }
        }

        fun bind(item: MovieEntity) = with(binding) {
            tvMovieTitle.text = item.title
            tvReleaseDate.text = item.releaseDate

            ivFavorite.setImageResource(if (item.isFavorite) R.drawable.ic_remove_favorite else R.drawable.ic_add_favorite)

            Glide.with(itemView.context).load(item.getImageUri())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivMoviePoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(
                oldItem: MovieEntity,
                newItem: MovieEntity
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MovieEntity,
                newItem: MovieEntity
            ): Boolean =
                oldItem == newItem
        }
    }

}