package com.da.domain.useCases

import com.da.domain.download.DownloadWorker

class CleanTempFilesUseCase(
    private val downloadWorker: DownloadWorker
) {
    suspend operator fun invoke() {
        downloadWorker.clearTempFiles()
    }
}