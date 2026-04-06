package com.da.domain.download

import com.da.domain.model.Download
import java.io.File


interface DownloadWorker {
    suspend fun clearTempFiles()

    /**
     * check is there pending download in entity
     * download proc have 3 retry
     */
    suspend fun checkPendingDownloads()

    suspend fun download(download: Download): Result<File>


}