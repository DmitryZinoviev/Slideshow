package com.da.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "downloads"
)
data class DownloadEntity(
    @PrimaryKey val creativeKey: String,
    val localPath: String?,
    val status: DownloadStatusEntity
)

enum class DownloadStatusEntity {
    NOT_DOWNLOADED,
    DOWNLOADING,
    DOWNLOADED,
    ERROR
}