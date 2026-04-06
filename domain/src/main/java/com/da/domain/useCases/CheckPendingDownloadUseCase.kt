package com.da.domain.useCases

import com.da.domain.repository.DownloadRepository

class CheckPendingDownloadUseCase(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke() {
        downloadRepository.checkPendingDownloads()

    }
}