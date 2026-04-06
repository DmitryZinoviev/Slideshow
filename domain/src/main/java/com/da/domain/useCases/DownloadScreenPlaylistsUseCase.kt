package com.da.domain.useCases

import com.da.domain.model.Download
import com.da.domain.repository.DownloadRepository

class DownloadScreenPlaylistsUseCase(
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(screenKey: String): DownloadResult{
        downloadRepository.downloadScreen(screenKey).onSuccess {
            val (count, totalCount) = it
            return if(count==totalCount)
                DownloadResult.Complete
            else if(count == 0)
                DownloadResult.Error
            else
                DownloadResult.Partial(count, totalCount)

        }.onFailure {
            return DownloadResult.Error
        }
        return DownloadResult.Error
    }
}