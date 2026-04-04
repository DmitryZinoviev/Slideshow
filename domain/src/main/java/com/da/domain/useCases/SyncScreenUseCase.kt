package com.da.domain.useCases

import com.da.domain.diff.ScreenDiffCalculator
import com.da.domain.model.FetchResult
import com.da.domain.model.Screen
import com.da.domain.model.ScreenResult
import com.da.domain.repository.PlaylistRepository

class SyncScreenUseCase(
    private val playlistRepository: PlaylistRepository,
    private val diffCalculator: ScreenDiffCalculator
) {
    suspend operator fun invoke(screenKey: String): FetchResult {

        val remote = playlistRepository.getRemoteScreen(screenKey)
        val local = playlistRepository.getLocalScreen(screenKey)

        if (local is ScreenResult.NotFound && remote is ScreenResult.Success) {
            val res = playlistRepository.fetchScreen(screen = remote.screen)
            if (res is ScreenResult.Success)
                return FetchResult.Fetch
        }

        if(local is ScreenResult.Success && remote is ScreenResult.Success){
            return update(local.screen, remote.screen)
        }

        return FetchResult.Error
    }

    private fun update(
        local: Screen,
        remote: Screen
    ): FetchResult {
        if(remote.modified == local.modified)
            return FetchResult.Skip

        val diffResult = diffCalculator.calculate(local, remote)

        playlistRepository.


        return FetchResult.Error
    }


}