package com.tasks.moviesapp.data.local.remote_key

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE movieId = :movieId")
    suspend fun getRemoteKeyByMovieId(movieId: Int): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()


    @Query("SELECT COUNT(*) FROM remote_keys")
    suspend fun countAllKeys(): Int
}