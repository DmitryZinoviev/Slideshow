package com.da.data.remote.network.mapper

import com.da.data.remote.network.dto.ScreenResponse
import com.da.domain.model.Screen

class ScreenMapper(
    private val playlistMapper: PlaylistMapper
) {

    fun map(from: ScreenResponse): Screen {
        return Screen(
            screenKey = from.screenKey.orEmpty(),
            playlists = from.playlists
                ?.mapNotNull { it?.let(playlistMapper::map) }
                .orEmpty(),
            modified = from.modified ?: 0L,
            isDownload = false
        )
    }
}