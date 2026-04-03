package com.da.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.da.data.local.entity.PlaylistEntity
import com.da.data.local.entity.PlaylistItemEntity

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists")
    suspend fun getPlaylistWithItems(): PlaylistEntity
}