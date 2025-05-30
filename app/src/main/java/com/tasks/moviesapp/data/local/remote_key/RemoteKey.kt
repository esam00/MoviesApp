package com.tasks.moviesapp.data.local.remote_key

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val movieId: Int,
    val nextPage: Int?
)
