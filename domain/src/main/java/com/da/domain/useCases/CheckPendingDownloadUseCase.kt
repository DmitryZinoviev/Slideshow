package com.da.domain.useCases

import com.da.domain.download.DownloadWorker

class CheckPendingDownloadUseCase(
    private val downloadWorker: DownloadWorker
) {
    suspend operator fun invoke() {
        downloadWorker.checkPendingDownloads()
    }
}