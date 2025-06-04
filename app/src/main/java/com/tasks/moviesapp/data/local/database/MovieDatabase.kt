package com.tasks.moviesapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tasks.moviesapp.data.local.dao.MovieDao
import com.tasks.moviesapp.data.local.entity.RemoteKey
import com.tasks.moviesapp.data.local.dao.RemoteKeysDao
import com.tasks.moviesapp.data.local.entity.MovieEntity

@Database(entities = [MovieEntity::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}