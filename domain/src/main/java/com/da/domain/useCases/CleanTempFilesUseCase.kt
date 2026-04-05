package com.da.domain.useCases

import com.da.domain.download.DownloadWorkerObserver

class CleanTempFilesUseCase(
    private val downloadWorkerObserver: DownloadWorkerObserver
) {
    suspend operator fun invoke() {
        downloadWorkerObserver.clearTempFiles()
    }
}