package com.da.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlists",
    foreignKeys = [
        ForeignKey(
            entity = ScreenEntity::class,
            parentColumns = ["screenKey"],
            childColumns = ["screenKey"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("screenKey")]
)
data class PlaylistEntity(
    @PrimaryKey
    val playlistKey: String,
    val screenKey: String,
    val isDownloaded: Boolean
)


