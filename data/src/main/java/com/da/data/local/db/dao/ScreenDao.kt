package com.da.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.da.data.local.db.entity.PlaylistEntity
import com.da.data.local.db.entity.PlaylistItemEntity
import com.da.data.local.db.entity.ScreenEntity
import com.da.data.local.db.entity.ScreenWithPlaylists

@Dao
interface ScreenDao {
    @Transaction
    @Query("SELECT * FROM screens WHERE screenKey = :screenKey")
    suspend fun getScreenWithPlaylists(screenKey: String): ScreenWithPlaylists?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScreen(screen: ScreenEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<PlaylistEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistItems(items: List<PlaylistItemEntity>)

    @Transaction
    suspend fun insertFullScreen(
        screen: ScreenEntity,
        playlists: List<PlaylistEntity>,
        items: List<PlaylistItemEntity>
    ) {
        insertScreen(screen)
        insertPlaylists(playlists)
        insertPlaylistItems(items)
    }

    @Transaction
    @Query("SELECT * FROM screens")
    suspend fun getScreens(): List<ScreenEntity>

    @Transaction
    @Query("SELECT * FROM screens where screenKey = :screenKey")
    suspend fun getScreenByKey(screenKey: String): ScreenEntity?


    @Transaction
    @Query("SELECT * FROM playlists")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM playlist_items")
    suspend fun getItems(): List<PlaylistItemEntity>

}