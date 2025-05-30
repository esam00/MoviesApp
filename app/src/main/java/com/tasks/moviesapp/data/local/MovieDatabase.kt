package com.tasks.moviesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tasks.moviesapp.data.local.remote_key.RemoteKey
import com.tasks.moviesapp.data.local.remote_key.RemoteKeysDao
import com.tasks.moviesapp.domain.model.MovieEntity

@Database(entities = [MovieEntity::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}