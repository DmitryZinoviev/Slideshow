package com.da.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "downloads"
)
data class DownloadEntity(
    @PrimaryKey val creativeKey: String,
    val localPath: String?,
    val status: DownloadStatus
)

enum class DownloadStatus {
    NOT_DOWNLOADED,
    DOWNLOADING,
    DOWNLOADED,
    ERROR
}