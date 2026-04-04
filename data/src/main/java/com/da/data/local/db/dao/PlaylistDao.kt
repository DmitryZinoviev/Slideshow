package com.da.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.da.data.local.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists")
    suspend fun getPlaylistWithItems(): PlaylistEntity
}