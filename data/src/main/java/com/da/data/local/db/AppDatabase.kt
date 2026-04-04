package com.da.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.da.data.local.db.dao.PlaylistDao
import com.da.data.local.db.dao.PlaylistItemDao
import com.da.data.local.db.dao.ScreenDao
import com.da.data.local.db.entity.PlaylistEntity
import com.da.data.local.db.entity.PlaylistItemEntity
import com.da.data.local.db.entity.ScreenEntity

@Database(
    entities = [ScreenEntity::class, PlaylistEntity::class, PlaylistItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun screenDao(): ScreenDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistItemDao(): PlaylistItemDao

}