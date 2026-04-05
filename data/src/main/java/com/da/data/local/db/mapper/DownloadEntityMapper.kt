package com.da.data.local.db.mapper

import com.da.data.local.db.entity.DownloadEntity
import com.da.data.local.db.entity.DownloadStatusEntity
import com.da.domain.model.Download
import com.da.domain.model.DownloadStatus

class DownloadEntityMapper {

    fun map(entity: DownloadEntity): Download {
        return Download(
            creativeKey = entity.creativeKey,
            localPath = entity.localPath,
            status = entity.status.toDomain()
        )
    }
}

fun DownloadStatusEntity.toDomain(): DownloadStatus = when (this) {
    DownloadStatusEntity.NOT_DOWNLOADED -> DownloadStatus.NOT_DOWNLOADED
    DownloadStatusEntity.DOWNLOADING -> DownloadStatus.DOWNLOADING
    DownloadStatusEntity.DOWNLOADED -> DownloadStatus.DOWNLOADED
    DownloadStatusEntity.ERROR -> DownloadStatus.ERROR
}

fun DownloadStatus.toEntity(): DownloadStatusEntity = when (this) {
    DownloadStatus.NOT_DOWNLOADED -> DownloadStatusEntity.NOT_DOWNLOADED
    DownloadStatus.DOWNLOADING -> DownloadStatusEntity.DOWNLOADING
    DownloadStatus.DOWNLOADED -> DownloadStatusEntity.DOWNLOADED
    DownloadStatus.ERROR -> DownloadStatusEntity.ERROR
}
