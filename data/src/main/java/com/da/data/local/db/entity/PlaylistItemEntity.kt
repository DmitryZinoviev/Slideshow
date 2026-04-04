package com.da.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist_items",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["playlistKey"],
            childColumns = ["playlistKey"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("playlistKey")]
)
data class PlaylistItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val duration: Int?,
    val dataSize: Int?,
    val modified: Long?,
    val creativeLabel: String?,
    val playlistKey: String,
    val creativeKey: String?,
    val orderKey: Int?
)