package com.da.domain.download


interface DownloadWorkerObserver {
    suspend fun clearTempFiles()

    /**
     * check is there pending download in entity
     * download proc have 3 retry
     */
    suspend fun checkPendingDownloads(): Any
}