package com.da.domain.useCases

import com.da.domain.model.ScreenResult
import com.da.domain.repository.PlaylistRepository

class SyncScreenUseCase(
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(screenKey: String) {
        val local = playlistRepository.getLocalScreen(screenKey)
        val remote = playlistRepository.getRemoteScreen(screenKey)
    }


}