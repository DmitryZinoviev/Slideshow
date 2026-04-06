package com.da.domain.useCases

import com.da.domain.repository.DownloadRepository

class DownloadScreenPlaylistsUseCase(
    private val downloadRepository: DownloadRepository
) {
    suspend fun invoke(screenKey: String): Boolean{
        downloadRepository.downloadScreen(screenKey)
    }
}