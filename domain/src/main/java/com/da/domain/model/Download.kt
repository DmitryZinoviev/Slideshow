package com.da.domain.model

data class Download(
    val creativeKey: String,
    val localPath: String?,
    val status: DownloadStatus
)

enum class DownloadStatus {
    NOT_DOWNLOADED,
    DOWNLOADING,
    DOWNLOADED,
    ERROR
}