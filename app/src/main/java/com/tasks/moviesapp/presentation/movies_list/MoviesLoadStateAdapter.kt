package com.tasks.moviesapp.presentation.movies_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tasks.moviesapp.R
import com.tasks.moviesapp.data.local.mapper.ViewType
import com.tasks.moviesapp.domain.model.Movie

class MoviesLoadStateAdapter(
    private val onItemClicked: (movieId: Int) -> Unit,
    private val onFavoriteClicked: (Int, Boolean) -> Unit
) : PagingDataAdapter<Movie, MoviesLoadStateAdapter.ViewHolder>(REPO_COMPARATOR) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieTitleTextView: TextView = itemView.findViewById(R.id.tv_movie_title)
        private val releaseDateTextView: TextView = itemView.findViewById(R.id.tv_release_date)
        private val favoriteImageView: ImageView = itemView.findViewById(R.id.iv_favorite)
        private val moviePosterImageView: ImageView = itemView.findViewById(R.id.iv_movie_poster)

        init {
            itemView.setOnClickListener {
                getItem(bindingAdapterPosition)?.let {
                    onItemClicked(it.id)
                }
            }

            favoriteImageView.setOnClickListener {
                getItem(bindingAdapterPosition)?.let {
                    onFavoriteClicked(it.id, !it.isFavorite)
                }
            }
        }

        fun bind(item: Movie) {
            movieTitleTextView.text = item.title
            releaseDateTextView.text = item.releaseDate

            favoriteImageView.setImageResource(if (item.isFavorite) R.drawable.ic_remove_favorite else R.drawable.ic_add_favorite)

            Glide.with(itemView.context).load(item.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(moviePosterImageView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = getItem(position)?.viewType ?: ViewType.Grid

        return when (viewType) {
            ViewType.Grid -> R.layout.item_movie_grid
            ViewType.List -> R.layout.item_movie_list
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean =
                oldItem == newItem
        }
    }

}