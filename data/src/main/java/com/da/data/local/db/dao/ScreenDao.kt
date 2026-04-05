package com.da.data.local.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.da.data.local.db.entity.DownloadEntity
import com.da.data.local.db.entity.DownloadStatusEntity
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDownloads(items: List<DownloadEntity>)


    @Transaction
    suspend fun insertFullScreen(
        screen: ScreenEntity,
        playlists: List<PlaylistEntity>,
        items: List<PlaylistItemEntity>
    ) {
        insertScreen(screen)
        insertPlaylists(playlists)
        insertPlaylistItems(items)
        Log.d("DB_DEBUG", "Inserted items: ${items.size}")

        val duplicates = items
            .filter { it.creativeKey != null }
            .groupBy { it.creativeKey }
            .filter { it.value.size > 1 }
            .flatMap { it.value }

        val downloads = items
            .filter { it.creativeKey != null }
            .map {
                DownloadEntity(
                    creativeKey = it.creativeKey!!,
                    localPath = null,
                    status = DownloadStatusEntity.NOT_DOWNLOADED
                )
            }

        Log.d("DB_DEBUG", "Downloads to insert: ${downloads.size}")

        try {


            insertDownloads(downloads)
        }catch (e: Exception){
            Log.e("DB_DEBUG", e.toString())
        }
        finally {
            Log.d("DB_DEBUG", "done")

        }
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

    @Transaction
    @Query("SELECT * FROM downloads")
    suspend fun getDownloads(): List<DownloadEntity>

}