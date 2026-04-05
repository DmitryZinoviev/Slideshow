package com.da.domain.useCases

import com.da.domain.download.DownloadWorkerObserver

class CheckPendingDownloadUseCase(
    private val downloadWorkerObserver: DownloadWorkerObserver
) {
    suspend operator fun invoke() {
        downloadWorkerObserver.checkPendingDownloads()
    }
}