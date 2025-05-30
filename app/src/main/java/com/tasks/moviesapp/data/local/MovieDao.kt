package com.tasks.moviesapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tasks.moviesapp.domain.model.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies ORDER BY originalIndex ASC")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun countAllMovies(): Int
}