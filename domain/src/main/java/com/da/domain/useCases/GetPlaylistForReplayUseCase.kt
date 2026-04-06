package com.da.domain.useCases

import com.da.domain.model.PlaylistForReplay
import com.da.domain.model.PlaylistForReplayResult
import com.da.domain.model.PlaylistItemForReplay
import com.da.domain.model.ScreenResult
import com.da.domain.repository.DownloadRepository
import com.da.domain.repository.PlaylistRepository

/**
 * Get last downloaded playlist
 * all playlist will be presented in list with order from screen
 */
class GetPlaylistForReplayUseCase(
        private val playlistRepository: PlaylistRepository,
    private val downloadRepository: DownloadRepository
) {
    suspend operator fun invoke(screenKey: String): PlaylistForReplayResult {
        if (screenKey.isNotEmpty()) {
            val screenResult = playlistRepository.getLocalScreen(screenKey)

            if (screenResult is ScreenResult.Success) {
                val screen = screenResult.screen
                val creativeKeys = screen.getAllPlaylistItems()
                    .map { it.creativeKey }
                    .distinct()
                val downloads = downloadRepository.getDownloads(creativeKeys)
                val items = mutableListOf<PlaylistItemForReplay>()
                var order = 0

                for (playlist in screen.playlists) {
                    for (i in playlist.items.sortedBy { it.orderKey }) {
                        val download = downloads.firstOrNull { it.creativeKey == i.creativeKey }
                        download?.localPath?.let {
                            items.add(
                                PlaylistItemForReplay(
                                    order = order++,
                                    path = it,
                                    isVideo = it.isVideoFile(),
                                    durationSec = i.duration
                                )
                            )
                        }
                    }
                }
                return PlaylistForReplayResult.Success(PlaylistForReplay(screenKey, items))
            } else {
                return PlaylistForReplayResult.Fail
            }
        }
        return PlaylistForReplayResult.Fail
    }

    fun String.isVideoFile(): Boolean {
        val ext = substringAfterLast('.', "").lowercase()
        return ext in listOf("mp4", "mkv")
    }

}