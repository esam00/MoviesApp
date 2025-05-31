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
import com.tasks.moviesapp.core.utils.getImageUri
import com.tasks.moviesapp.domain.model.MovieEntity
import com.tasks.moviesapp.presentation.movies_list.ui_state.PreviewType

class MoviesLoadStateAdapter(
    private var previewType: PreviewType,
    private val onItemClicked: (movieId: Int) -> Unit,
    private val onFavoriteClicked: (Int, Boolean) -> Unit
) : PagingDataAdapter<MovieEntity, MoviesLoadStateAdapter.ViewHolder>(REPO_COMPARATOR) {

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

        fun bind(item: MovieEntity){
            movieTitleTextView.text = item.title
            releaseDateTextView.text = item.releaseDate

            favoriteImageView.setImageResource(if (item.isFavorite) R.drawable.ic_remove_favorite else R.drawable.ic_add_favorite)

            Glide.with(itemView.context).load(item.getImageUri())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(moviePosterImageView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (previewType) {
            PreviewType.GRID -> R.layout.item_movie_grid
            PreviewType.LIST -> R.layout.item_movie_list
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

    fun notifyPreviewTypeChange(newPreviewType: PreviewType) {
        previewType = newPreviewType
        notifyDataSetChanged()
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