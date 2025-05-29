package com.tasks.moviesapp.data.paging_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tasks.moviesapp.data.remote.MoviesApiService
import com.tasks.moviesapp.domain.model.MovieEntity
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class MoviesPagingSource(private val apiServices: MoviesApiService) :
    PagingSource<Int, MovieEntity>() {

    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        Log.i("TAG", "load: position: ${params.key}")

        val page = params.key ?: STARTING_PAGE_INDEX
        var hasMorePages = false

        return try {
            // Fetch Movies from the network
            val response = apiServices.getAllMovies(page)
            response.body()?.page?.let {
                hasMorePages = it <= 500
            }

            LoadResult.Page(
                data = response.body()?.results ?: emptyList(),
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (hasMorePages) page + 1 else null
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}